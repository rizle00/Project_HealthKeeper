package kr.co.app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.co.app.member.MemberService;
import kr.co.app.member.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@Controller @RequestMapping("/test")
public class testController {
	
	@Autowired
	private MemberService service;

	
	@RequestMapping("insert") @ResponseBody
	public void insert(String params,@RequestParam(defaultValue = "2") int member_id ) {
		HashMap<String, Object> map = new Gson().fromJson(params, new TypeToken<HashMap<String,Object>>(){}.getType());
//		map.put("heart",Integer.parseInt(map.get("heart").toString()));
		System.out.println("hr"+map.get("heart"));
		System.out.println("temp"+map.get("temp"));
		System.out.println("acc"+map.get("accident"));
//		System.out.println((int)Double.parseDouble(map.get("heart").toString()));
		if(map.get("accident").equals("1")){
			map.put("accident", "Y");
		} else map.put("accident", "N");
		map.put("member_id", member_id);
		System.out.println(map.get("accident"));
		System.out.println(service.insert(map));

	}

	
	@PostMapping("/andlogin")
	public ResponseEntity<String> login(String member_id, String pw) {
		System.out.println("요청");
		MemberVO vo = service.login(member_id);
		System.out.println(member_id.toString() + pw.toString());
		if(vo.getPw().equals(pw)){//나중에 encoding해야함
			return ResponseEntity.ok(new Gson().toJson(vo));}
		else {
			return null;
		}
	} 
	
	@PostMapping(value="/andidcheck" )
	public ResponseEntity<String> idcheck(String member_id) {
		System.out.println("서비스"+service.idcheck(member_id));
		String result = service.idcheck(member_id);
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
	
	@PostMapping("/sociallogin")
	public ResponseEntity<String> kakaologin(String social) {
//		MemberVO kakaoVo = new Gson().fromJson(vo, MemberVO.class);
		
		if(service.socialIdCheck(social).equals("0")) {
			return ResponseEntity.ok("join");
		}else {
			String returnVO = new Gson().toJson(service.socialLogin(social));
			System.out.println(returnVO);
			return ResponseEntity.ok(returnVO);
		}
	}
	
	
	
	@RequestMapping("/andfindpw")
	public void resetpw(String vo) {
		MemberVO find_info = new Gson().fromJson(vo,MemberVO.class);
		String pw = UUID.randomUUID().toString();
		find_info.setPw(pw);
		
		if(service.resetpw(find_info)==1) {
			
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
	
	
	
}
