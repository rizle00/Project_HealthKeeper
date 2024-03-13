package kr.co.mapper;

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
public class QsReplyMapperTest {
	
	private static final Logger log = LoggerFactory.getLogger(QsReplyMapperTest.class); 
	
	@Autowired
	private QsReplyMapper mapper;
	
	// 질문게시판 댓글수정 테스트
//	@Test
//	public void testReply() {
//		
//		QsReplyVO vo = new QsReplyVO();
//		
//		vo.setQRNO(4);
//		vo.setCONTENT("mapper 테스트중");
//		
//		int result = mapper.updateReply(vo);
//		log.info("result" + result);
//	}
	
	// 질문게시판 댓글삭제 테스트
//	@Test
//	public void deletereply() {
//		
//		int result = mapper.deleteReply(1);
//		log.info("result" + result);
//	}
}
