package kr.co.app;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.google.gson.Gson;

import kr.co.app.member.MemberService;
import kr.co.app.member.MemberVO;
import oracle.sql.CharacterBuffer;

@RequestMapping("and")
@Controller
public class MemberController {

	@Autowired
	private MemberService service;

	@PostMapping("/andlogin")
	public ResponseEntity<String> login(String email, String pw) {
		System.out.println("요청");
		MemberVO vo = service.login(email);
		System.out.println(email.toString() + pw.toString());
		if (vo.getPw().equals(pw)) {// 나중에 encoding해야함
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

	@RequestMapping("/andfindpw")
	public void resetpw(String vo) {
		MemberVO find_info = new Gson().fromJson(vo, MemberVO.class);
		String pw = UUID.randomUUID().toString();
		find_info.setPw(pw);

		if (service.resetpw(find_info) == 1) {

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

	@PostMapping("")
	public void socialCheck(String email) {
		service.socialIdCheck(email);
	}
}
