package kr.co.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.model.QsCriteria;
import kr.co.model.QsVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class QsServiceTests {
	
	private static final Logger log = LoggerFactory.getLogger(QsServiceTests.class);
	
	@Autowired
	private QsService service;
	
	// 게시판 등록 테스트
//	@Test
//	public void testEnroll() {
//		
//		QsVO vo = new QsVO();
//		
//		vo.setQS_TITLE("service Test 중");
//		vo.setQS_CONTENT("service Test 중입니다.");
//		vo.setQS_WRITER("service 유저");
//		
//		service.qsregistr(vo);
//	}
	
	// 게시판 목록 테스트
//	@Test
//	public void testList() {
//		service.getlist().forEach(qs -> log.info("" + qs));
//	}
	
	// 게시판 조회 테스트
//	@Test
//	public void testpage() {
//		
//		int QS_BNO = 1;
//		
//		log.info("" + service.getpage(QS_BNO));
//	}
	
	// 게시판 수정 테스트
//	@Test
//	public void update() {
//		QsVO vo = new QsVO();
//		
//		vo.setQS_BNO(1);
//		vo.setQS_TITLE("게시판 service Test");
//		vo.setQS_CONTENT("게시판 service Test 중입니다.");
//		
//		int result = service.update(vo);
//		log.info("result : " + result);
//	}
	
	// 게시판 삭제 테스트
//	@Test
//	public void delete() {
//		
//		int result = service.delete(7);
//		
//		log.info("result : " + result);
//	}
	
	// 게시판 목록 (페이징 적용)
	@Test
	public void listPaging() {
		
		QsCriteria qcri = new QsCriteria();
		
		qcri.setPageNum(2);
		
		List list = service.getlistPaging(qcri);
		
		list.forEach(qs -> log.info("" + qs));
	}

}
