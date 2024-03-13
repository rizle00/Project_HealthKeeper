package kr.co.mapper;

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
public class QsMapperTests {
	
	private static final Logger log = LoggerFactory.getLogger(QsMapperTests.class);

	@Autowired
	private QsMapper mapper;
	
	
	// 게시판 등록 테스트
//	@Test
//	public void testEnroll() {
//		
//		QsVO vo = new QsVO();
//		
//		vo.setQS_TITLE("게시판 Mapper 등록 테스트중.");
//		vo.setQS_CONTENT("게시판 Mapper 등록테스트중입니다.");
//		vo.setQS_WRITER("일반유저 김김김");
//		
//		mapper.qsregistr(vo);
//	}
	
	// 게시판 목록 테스트
//	@Test
//	public void testList() {
//		
//		List list = mapper.getlist();
//		
//		for(int i=0; i<list.size(); i++) {
//			log.info("" + list.get(i));
//		}
//	}
	
	// 게시판 조회 테스트
//	@Test
//	public void testpage() {
//		
//		int QS_BNO = 1;
//		
//		log.info("" + mapper.getpage(QS_BNO));
//	}
	
	// 게시판 수정 테스트
//	@Test
//	public void testUpdate() {
//		
//		QsVO vo = new QsVO();
//		
//		vo.setQS_BNO(1);
//		vo.setQS_TITLE("게시판 mapper Test");
//		vo.setQS_CONTENT("게시판 mapper Test 중입니다.");
//		
//		int result = mapper.update(vo);
//		log.info("result" + result);
//	}
	
	// 게시판 삭제 테스트
//	@Test
//	public void testdelete(){
//		int result = mapper.delete(5);
//		log.info("result : " + result);
//	}
	
	// 게시판 페이징 테스트
	@Test
	public void listpaging() {
		QsCriteria qcri = new QsCriteria();
		
		qcri.setPageNum(2);
		
		List list = mapper.getlistPaging(qcri);
		
		list.forEach(qs -> log.info("" + qs));
	}
}
