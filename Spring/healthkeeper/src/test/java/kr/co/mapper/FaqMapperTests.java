package kr.co.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.model.FaqVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class FaqMapperTests {
	
	private static final Logger log = LoggerFactory.getLogger(FaqMapperTests.class);
	
	@Autowired
	private FaqMapper mapper;
	
	// faq 게시판 등록테스트
//	@Test
//	public void faqregistr() {
//		
//		FaqVO vo = new FaqVO();
//		
//		vo.setFAQ_TITLE("faq mapper test");
//		vo.setFAQ_CONTENT("faq mapper test 중");
//		vo.setFAQ_WRITER("faq 관리자");
//		
//		mapper.faqregistr(vo);
//	}
	
	// FAQ 게시판 목록테스트
//	@Test
//	public void faqlist() {
//		
//		List list = mapper.faqlist();
//		
//		for(int i=0; i<list.size(); i++) {
//			log.info("" + list.get(i));
//		}
//	}
	
	// FAQ 게시판 수정테스트
//	@Test
//	public void faqupdate() {
//		
//		FaqVO vo = new FaqVO();
//		
//		vo.setFAQ_ID(10);
//		vo.setTITLE("mapper Test 진행");
//		vo.setCONTENT("mapper 수정테스트중입니다.");
//		
//		int result = mapper.faqupdate(vo);
//		log.info("result: " + result);
//	}
	
	// FAQ 게시판 조회테스트
//	@Test
//	public void faqpage() {
//		
//		int FAQ_ID = 10;
//		log.info("" + mapper.faqpage(FAQ_ID));
//	}

}
