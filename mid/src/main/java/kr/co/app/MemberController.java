package kr.co.app;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import kr.co.app.common.DataHolder;
import kr.co.app.config.FirebaseCloudMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import kr.co.app.common.CommonUtility;
import kr.co.app.member.MemberService;
import kr.co.app.member.MemberVO;

@RequestMapping("and")
@Controller
public class MemberController {
	@Autowired private CommonUtility common;
	@Autowired private BCryptPasswordEncoder pwEncoder;

	@Autowired
	private MemberService service;

	@Autowired
	private DataHolder dataHolder;

	@PostMapping("/andlogin")
	public ResponseEntity<String> login(String email, String pw) {
		System.out.println("요청");
		MemberVO vo = service.login(email);
		
		System.out.println(email.toString() + pw.toString());
		if (pwEncoder.matches(pw, vo.getPw())) {
			dataHolder.setData(vo.getToken());
			return ResponseEntity.ok(new Gson().toJson(vo));
		} else {
			return null;
		}
	}

	@PostMapping(value = "/andidcheck")
	public ResponseEntity<String> idcheck(String email) {
		System.out.println("서비스" + service.idcheck(email));
		String result = service.idcheck(email);
		return ResponseEntity.ok(result);
	}

	@RequestMapping("/andjoin") // 되었는지 안되었는지 정보 보내줘야겠다
	public void join(String vo, String type) {
		System.out.println(type + "으로 가입");
		MemberVO info = new Gson().fromJson(vo, MemberVO.class);
		info.setPw(pwEncoder.encode(info.getPw()));
		if (type.equals("patient")) {
			service.join(info);
		} else {
			String patient = info.getGuardian_id();
			info.setGuardian_id(null);
			service.join(info);
			info.setGuardian_id(patient);
			service.patientRegister(info);
		}
	}

	@PostMapping("/andfindid")
	public ResponseEntity<String> findid(String vo) {
		MemberVO find_info = new Gson().fromJson(vo, MemberVO.class);
		return ResponseEntity.ok(service.findid(find_info));
	}

	@PostMapping(value = "/sociallogin", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> kakaologin(String social) {
		if (service.socialIdCheck(social).equals("0")) {
			return ResponseEntity.ok("join");
		} else {
			String returnVO = new Gson().toJson(service.socialLogin(social));
			System.out.println(returnVO);
			return ResponseEntity.ok(returnVO);
		}
	}

	@RequestMapping("/checkinfo")
	public ResponseEntity<String> checkinfo(String vo,String mail) {
		MemberVO find_info = new Gson().fromJson(vo, MemberVO.class);
		if(service.findpw(find_info).equals("0")) {
			return ResponseEntity.ok("none");
		}else {
			String pw = UUID.randomUUID().toString();
			pw= pw.substring(pw.lastIndexOf("-")+1);
			find_info.setPw(pw);
			
			if(service.resetpw(find_info)==1&&common.sendPassword(mail,pw))
			return ResponseEntity.ok("success");
			else return ResponseEntity.ok("failure");
		}
	}

	@RequestMapping("partnercheck")
	public ResponseEntity<String> partnerCheck(String partner_id) {
		return ResponseEntity.ok(service.partnerCheck(partner_id));
	}

	@RequestMapping("/address")
	public String address() {
		return "daum";
	}
	
	@PostMapping("/andmodify")
	public void modify(MemberVO vo) {
		service.modify(vo);
	}
}
