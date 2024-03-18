package kr.co.app;

import com.google.gson.Gson;

import kr.co.app.community.CommunityService;
import kr.co.app.community.FaqVO;
import kr.co.app.community.NoticeVO;
import kr.co.app.community.QueVO;
import kr.co.app.member.MemberService;
import kr.co.app.member.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class CommunityController {
	
	@Autowired
	private CommunityService service;


	
	@PostMapping(value = "question/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> list(String params) {
		System.out.println(params);
	List<QueVO>  result =	service.list();

    String json = new Gson().toJson(result);

		return ResponseEntity.ok(json);
	} 
	
	@PostMapping(value="faq/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> faq(String params) {
		System.out.println(params);
	List<FaqVO>  result =	service.faq();
    String json = new Gson().toJson(result);

		return ResponseEntity.ok(json);
	}
	
	@PostMapping(value="notice/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> notice(String params){
		System.out.println(params);
		List<NoticeVO> result = service.notice();
		 String json = new Gson().toJson(result);

			return ResponseEntity.ok(json);
		
	}

	
	
}
