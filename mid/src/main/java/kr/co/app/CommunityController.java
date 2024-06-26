package kr.co.app;

import com.google.gson.Gson;

import kr.co.app.community.*;
import kr.co.app.member.MemberService;
import kr.co.app.member.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class CommunityController {
	
	@Autowired
	private CommunityService service;
	
	
	@PostMapping(value = "question/answer", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> answer(String params) {
		System.out.println(params);
	List<AnswerVO>  result =	service.answer(params);
		System.out.println(result.size());
    String json = new Gson().toJson(result);

		return ResponseEntity.ok(json);
	} 

	
//	  @PostMapping(value = "question/que4", produces ="application/text;charset=utf-8") 
//	  public ResponseEntity<String> list4(String params) {
////		  System.out.println(params);
//		  List<QueVO> result = service.que4();
//	  System.out.println(result.size()); String json = new Gson().toJson(result);
//	  
//	  return ResponseEntity.ok(json); }
	
	
	
	//자유게시판의 내용과 답변에관련된 정보가져오기
	@PostMapping(value = "question/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> list(@RequestParam(defaultValue = "0") String params) {
//		System.out.println(params);
		
	List<QueVO>  result =	service.list(params);
	result.get(0).getQUE_ID();
	result.get(0).getAnswer().getQUE_ID();
	System.out.println("qqq"+result.size());
	

    String json = new Gson().toJson(result);
    System.out.println(json);

		return ResponseEntity.ok(json);
	} 
	
	
	//자유게시판에서 글쓰기 insert!
	@PostMapping(value="question/newWrite", produces = "applicateion/text;charset=utf-8")
	public ResponseEntity<Integer> newWrite(String params){
		QueVO vo = new Gson().fromJson(params, QueVO.class);


		return ResponseEntity.ok(service.newWrite(vo));
		
		
	}
	
	
	@PostMapping(value="faq/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> faq(String params) {
//		System.out.println(params);
	List<FaqVO>  result =	service.faq();
    String json = new Gson().toJson(result);

		return ResponseEntity.ok(json);
	}
	
	@PostMapping(value="notice/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> notice(String params){
//		System.out.println(params);
		List<NoticeVO> result = service.notice();
		 String json = new Gson().toJson(result);

			return ResponseEntity.ok(json);
		
	}

	@PostMapping(value="category", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> category(String params){
//		System.out.println(params);
		List<CategoryVO> result = service.category();
		String json = new Gson().toJson(result);

		return ResponseEntity.ok(json);

	}

	
	
}
