package kr.co.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.model.QsReplyVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class QsReplyServiceTest {
	
	private static final Logger log = LoggerFactory.getLogger(QsReplyServiceTest.class);
	
	@Autowired
	private QsReplyService service;
	
	// 질문게시판 댓글수정 테스트
//	@Test
//	public void testService() {
//		
//		QsReplyVO vo = new QsReplyVO();
//		
//		vo.setQRNO(4);
//		vo.setCONTENT("service Test");
//		
//		int result = service.updateReply(vo);
//		log.info("result" + result);
//	}
	
	// 질문게시판 댓글삭제 테스트
//	@Test
//	public void deletereply() {
//		
//		int result = service.deleteReply(5);
//		log.info("result" + result);
//	}

}
