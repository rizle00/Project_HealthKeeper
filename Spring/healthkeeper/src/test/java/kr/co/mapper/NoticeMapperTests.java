package kr.co.mapper;

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
public class NoticeMapperTests {
 
     private static final Logger log = LoggerFactory.getLogger(NoticeMapperTests.class);
     
     @Autowired
     private NoticeMapper mapper;
 
     // 공지사항 등록 테스트
     @Test
     public void testEnroll() {
         
         NoticeVO vo = new NoticeVO();
         
         vo.setTITLE("공지사항 Mapper 테스트.");
         vo.setCONTENT("공지사항 Mapper 테스트중입니다.");
         vo.setMEMBER_ID(2);
         
         mapper.notregistr(vo);
     }
     
     // 공지사항 목록 테스트
//     @Test
//     public void testGetList() {
//         
//         List list = mapper.getlist();
//        /* 일반적 for문 */
//         for(int i = 0; i < list.size();i++) {
//             log.info("" + list.get(i));
//         }
//     }
     
     // 공지사항 조회 테스트
//     @Test
//     public void testGetPage() {
//         
//         /* 실제 존재하는 페이지 */
//         int NOTICE_BNO = 10;
//         
//         log.info("" + mapper.getPage(NOTICE_BNO));
//         
//     }
     
     // 공지사항 수정 테스트
//     @Test
//     public void testnotModify() {
//         
//         NoticeVO notice = new NoticeVO();
//
//         notice.setNOTICE_BNO(10);
//         notice.setNOTICE_TITLE("Mapper Test22");
//         notice.setNOTICE_CONTENT("Mapper Test 진행중입니다.");
//         
//         int result = mapper.notmodify(notice);
//         log.info("result : " +result);
//         
//     }
     
     // 공지사항 삭제 테스트
//     @Test
//     public void testDelete() {
//         
//         int result = mapper.notdelete(9);
//         log.info("result : " + result);
//         
//     }
     
     // 공지사항 목록(페이징 적용) 테스트
//     @Test
//     public void testGetListPaging() {
//         
//         NotCriteria ncri = new NotCriteria();
//         
//         ncri.setPageNum(2);
//                          
//         List list = mapper.getListPaging(ncri);
//         
//         list.forEach(notice -> log.info("" + notice));
//     }
 
}
