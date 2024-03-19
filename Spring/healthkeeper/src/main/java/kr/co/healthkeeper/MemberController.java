package kr.co.healthkeeper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.util.JSONPObject;
import kr.co.common.CommonUtility;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.model.MemberVO;
import kr.co.service.MemberService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

@Controller
@PropertySource("classpath:info.properties") //카카오 앱 키 저장용
@RequestMapping("/member/*")
public class MemberController {
	@Autowired
	CommonUtility common;

	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder pwEncoder;
	
	// 회원가입 페이지 이동 코드
	@GetMapping("/join")
	public void memberjoinGET() {
		log.info("회원가입 페이지 진입");
	}
		
	// 회원가입
	@PostMapping("/join")
	public String memberjoinPOST(MemberVO member) throws Exception {
			
		String rawPw = "";            // 인코딩 전 비밀번호
	    String encodePw = "";        // 인코딩 후 비밀번호
	        
	    rawPw = member.getPW();          // 비밀번호 데이터 얻음
	    encodePw = pwEncoder.encode(rawPw);        // 비밀번호 인코딩
	    member.setPW(encodePw);            // 인코딩된 비밀번호 member객체에 다시 저장
	        
	    /* 회원가입 쿼리 실행 */
	    memberService.memberjoin(member);
			
	    return "redirect:/main";
	}
	
	// 로그인 페이지 이동코드
	@GetMapping("/login")
	public void memberloginGET(){
		log.info("로그인 페이지 진입");
	}
	
	// 로그인
	@PostMapping("/login")
	public String memberloginPOST
	(MemberVO member,RedirectAttributes rttr, HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		String rawPw = "";            // 인코딩 전 비밀번호
        String encodePw = "";        // 인코딩 후 비밀번호
        
		MemberVO lvo = memberService.memberlogin(member); // 제출한 아이디와 일치하는 아이디가 있는지 확인
		
		// 일치하는 아이디 존재시
		if(lvo != null) {
			rawPw = member.getPW(); // 사용자가 제출한 비밀번호
			encodePw = lvo.getPW(); // DB에 저장한 인코딩된 비밀번호
			
			if(true == pwEncoder.matches(rawPw, encodePw)) { // 두 개의 메서드의 비밀번호 일치여부 판단
				
				lvo.setPW(""); // 인코딩된 비밀번호 정보를 지움
				session.setAttribute("member", lvo); // 세션에 사용자의 정보를 저장
				return "redirect:/main"; // 메인페이지로 이동
				
			}else {
				rttr.addFlashAttribute("result", 0);
				return "redirect:/member/login";
			}
		}else { // 일치하는 아이디가 존재하지 않을 시 ( 로그인 실패 )
			
			rttr.addFlashAttribute("result",0);
			return "redirect:/member/login";
		}
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logoutGET(HttpServletRequest request) throws Exception {
		log.info("로그아웃 메서드 진입");
		
		HttpSession session = request.getSession();
		session.invalidate(); // 세션 전체를 무효화하는 메서드, 세션 제거
		
		return "redirect:/main";
	}

	@Value("${KAKAO_REST_API}") private  String KAKAO_REST_API;
	@Value("${NAVER_CLIENT_ID}") private String NAVER_CLIENT_ID;
	@Value("${NAVER_SECRET}") private String NAVER_SECRET;
	@RequestMapping("kakaoLogin")
	public String kakaoLogin(HttpServletRequest request){
		StringBuffer url = new StringBuffer("https://kauth.kakao.com/oauth/authorize?response_type=code");
		url.append("&client_id=").append(KAKAO_REST_API);
		url.append("&redirect_uri=").append(common.appURL(request)).append("/member/kakaocallback");//https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code
		return "redirect:" + url.toString();
	}

	@RequestMapping("kakaocallback")
	public String kakaocallback(String code,HttpSession session, Model model){
		if(code ==null) return "redirect:/main";

		StringBuffer url = new StringBuffer("https://kauth.kakao.com/oauth/token?grant_type=authorization_code");
		url	.append( "&client_id=" ).append( KAKAO_REST_API )
				.append( "&code=" ).append( code );
		String response = common.requestAPI(url.toString());
		JSONObject json = new JSONObject(response);
		String token_type = json.getString("token_type");
		String access_token = json.getString("access_token");

		response = common.requestAPI("https://kapi.kakao.com/v2/user/me", token_type +" "+access_token);
		json =  new JSONObject(response);
		if(! json.isEmpty()) {
			String social = String.valueOf(json.getLong("id"));
			json = json.getJSONObject("kakao_account");

			MemberVO vo = new MemberVO();
			vo.setSOCIAL(social);
			session.setAttribute("social",social);
		}
		return "redirect:/main/member/join";
	}


	@RequestMapping("naverlogin")
	public String naverLogin(HttpSession session, HttpServletRequest request){
		String state = UUID.randomUUID().toString();
		session.setAttribute("state",state);

		StringBuffer url = new StringBuffer("https://nid.naver.com/oauth2.0/authorize?response_type=code");
		url.append("&client_id=").append(NAVER_CLIENT_ID);

		return null;
	}
}