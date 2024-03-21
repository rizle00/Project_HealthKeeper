package kr.co.and;

import com.google.gson.Gson;
import kr.co.model.CateGoryVO;
import kr.co.model.FaqVO;
import kr.co.model.NoticeVO;
import kr.co.model.QsVO;
import kr.co.service.AndCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequestMapping("/and")
public class CommunityController {
	
	@Autowired
	private AndCommunityService service;
	
	

	
	@PostMapping(value = "question/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> list(@RequestParam(defaultValue = "0") String params) {
		System.out.println("질문");
		
	List<QsVO>  result =	service.list(params);
	System.out.println("질문"+result.size());
	

    String json = new Gson().toJson(result);
    System.out.println(json);

		return ResponseEntity.ok(json);
	}


	//자유게시판에서 글쓰기 insert!
	@PostMapping(value="question/newWrite", produces = "applicateion/text;charset=utf-8")
	public ResponseEntity<Integer> newWrite(String params){
		System.out.println("글쓰기");
		QsVO vo = new Gson().fromJson(params, QsVO.class);
		System.out.println("글쓰기"+params);

		return ResponseEntity.ok(service.newWrite(vo));


	}

	@PostMapping(value="category", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> category(){
		System.out.println("카테고리");
		List<CateGoryVO> result = service.category();
		System.out.println("카테고리"+result.size());
		String json = new Gson().toJson(result);

		return ResponseEntity.ok(json);

	}

	@PostMapping(value="faq/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> faq() {
		System.out.println("faq");
	List<FaqVO>  result =	service.faq();
		System.out.println("faq"+result.size());
    String json = new Gson().toJson(result);

		return ResponseEntity.ok(json);
	}
	
	@PostMapping(value="notice/list", produces = "application/text;charset=utf-8")
	public ResponseEntity<String> notice(){
		System.out.println("공지");
		List<NoticeVO> result = service.notice();
		System.out.println("공지"+result.size());
		 String json = new Gson().toJson(result);

			return ResponseEntity.ok(json);
		
	}

	
	
}
