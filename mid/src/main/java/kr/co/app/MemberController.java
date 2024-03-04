package kr.co.app;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kr.co.app.member.MemberService;
import kr.co.app.member.MemberVO;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService service;

	
	@RequestMapping("test")
	public void con() {
		System.out.println("요청");
	}

	
	@PostMapping("/andlogin")
	public ResponseEntity<String> login(String guardian_id, String guardian_pw) {
		System.out.println("요청");
		MemberVO vo = service.login(guardian_id);
		System.out.println(guardian_id.toString() + guardian_pw.toString());
		if(vo.getGuardian_pw().equals(guardian_pw)){//나중에 encoding해야함
			return ResponseEntity.ok(new Gson().toJson(vo));}
		else {
			return null;
		}
	} 
	
	@PostMapping(value="/andidcheck" )
	public ResponseEntity<String> idcheck(String guardian_id) {
		System.out.println("서비스"+service.idcheck(guardian_id));
		String result = service.idcheck(guardian_id);
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping("/andjoin") //되었는지 안되었는지 정보 보내줘야겠다
	public void join(String vo) {
		MemberVO info = new Gson().fromJson(vo, MemberVO.class);
		service.join(info);
	}
	
	@PostMapping("/andfindid")
	public ResponseEntity<String> findid(String vo){
		MemberVO find_info = new Gson().fromJson(vo, MemberVO.class);
		return ResponseEntity.ok(service.findid(find_info));
	}
	
	@RequestMapping("/andfindpw")
	public void resetpw(String vo) {
		MemberVO find_info = new Gson().fromJson(vo,MemberVO.class);
		String pw = UUID.randomUUID().toString();
		find_info.setGuardian_pw(pw);
		
		if(service.resetpw(find_info)==1) {
			
		}
		
	}
}
