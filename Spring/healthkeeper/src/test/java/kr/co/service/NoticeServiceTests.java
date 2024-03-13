package kr.co.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.model.NotCriteria;
import kr.co.model.NoticeVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class NoticeServiceTests {
	
	private static final Logger log = LoggerFactory.getLogger(NoticeServiceTests.class);
    
    @Autowired
    private NoticeService service;
    
    // 공지사항 등록 테스트
     @Test
     public void testEnroll() throws Exception{
        
        NoticeVO vo = new NoticeVO();
        
        vo.setTITLE("공지사항 service 테스트");
        vo.setCONTENT("공지사항 service 테스트중입니다.");
        vo.setMEMBER_ID(2);
        
       service.notregistr(vo);
    }
    
    // 공지사항 목록 테스트
//    @Test
//    public void testGetList() {
//        
//        service.getlist().forEach(board -> log.info("" + board));        
//        
//    }
    
    // 공지사항 조회 테스트
//    @Test
//    public void testGetPage() {
//    	
//    	int NOTICE_BNO = 10;
//    	
//    	log.info("" + service.getPage(NOTICE_BNO));
//    	
//    }
    
    // 공지사항 수정 테스트
//    @Test
//    public void testModify() {
//        
//        NoticeVO notice = new NoticeVO();
//        
//        notice.setNOTICE_BNO(10);
//        notice.setNOTICE_TITLE("Service Test333");
//        notice.setNOTICE_CONTENT("Service Test중입니다.");
//        
//        int result = service.notmodify(notice);
//        log.info("result : " +result);
//        
//    } 
    
    // 공지사항 삭제 테스트
//    @Test
//    public void testDelete() {
//        
//        int result = service.notdelete(8);
//        log.info("result : " + result);
//        
//    }
    
    // 공지사항 목록(페이징 적용) 테스트
//    @Test
//    public void testGetListPaging() {
//        
//        NotCriteria ncri = new NotCriteria();
//        
//        List list = service.getListPaging(ncri);
//        
//        list.forEach(notice -> log.info("" + notice));
//    }
    
}
