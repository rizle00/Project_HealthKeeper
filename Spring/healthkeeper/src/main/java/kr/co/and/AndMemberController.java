package kr.co.and;

import com.google.gson.Gson;
import kr.co.model.DiseaseVO;
import kr.co.model.HospitalVO;
import kr.co.model.MemberHospitalVO;
import kr.co.model.MemberVO;
import kr.co.service.AndMemberService;
import kr.co.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequestMapping("/and")
@Controller
public class AndMemberController {
	@Resource(name="commonUtil")private CommonUtil common;
//	@Autowired
	private BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();

	@Autowired
	private AndMemberService service;


	@PostMapping("/andlogin")
	public ResponseEntity<String> login(String email, String pw) {
		System.out.println("요청");
		MemberVO vo = service.login(email);

		System.out.println(email.toString() + pw.toString());
		if (pwEncoder.matches(pw, vo.getPW())) {
//			dataHolder.setData(vo.getToken());
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
		info.setPW(pwEncoder.encode(info.getPW()));
		if (type.equals("patient")) {
			service.join(info);
		} else {
			String patient = info.getGUARDIAN_ID();
			info.setGUARDIAN_ID(null);
			service.join(info);
			info.setGUARDIAN_ID(patient);
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
			find_info.setPW(pw);

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
	public ResponseEntity<String>  modify(String vo) {
		MemberVO infoVO = new Gson().fromJson(vo, MemberVO.class);
		infoVO.setPW(pwEncoder.encode(infoVO.getPW()));
		if(service.modify(infoVO)==1) {
			return ResponseEntity.ok("success");
		}else {
			return ResponseEntity.ok("fail");
		}
	}

	@PostMapping("/member/disease")
	public Integer disease(String params) {
		System.out.println(params);
		DiseaseVO vo = new Gson().fromJson(params, DiseaseVO.class);
		System.out.println(vo.getDISEASE_NAME());
		return service.insertDisease(vo);
	}

	@RequestMapping(value = "/member/hospitals", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> hospital(String name) {
		List<HospitalVO> list = service.hospitalList(name);
		System.out.println(list.size());

		return ResponseEntity.ok(new Gson().toJson(list));}
	@RequestMapping(value = "/member/doctors", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> doctors(String params) {

		MemberHospitalVO vo = new Gson().fromJson(params, MemberHospitalVO.class);
		List<String> list = service.doctorsList(vo);

		System.out.println(list.size());

		return ResponseEntity.ok(new Gson().toJson(list));}


	@RequestMapping(value = "/member/condition", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> condition(String params) {
		System.out.println(params);

		HashMap<String, Object> map = service.condition(params);

		System.out.println(map);

		return ResponseEntity.ok(new Gson().toJson(map));}





}

