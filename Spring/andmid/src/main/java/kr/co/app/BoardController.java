package kr.co.app;

import com.google.gson.Gson;
import kr.co.app.member.MemberService;
import kr.co.app.member.MemberVO;
import kr.co.app.member.QueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class BoardController {
	
	@Autowired
	private MemberService service;


	
	@PostMapping(value = "/question/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> list(String params) {
		System.out.println(params);
	List<QueVO>  result =	service.list();

 String json = new Gson().toJson(result);

		return ResponseEntity.ok(json);
	} 

	
	
}
