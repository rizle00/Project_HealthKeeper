package kr.co.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.mapper.NoticeMapper;
import kr.co.model.FilesVO;
import kr.co.model.MemberVO;
import kr.co.model.NotCriteria;
import kr.co.model.NoticeVO;
import kr.co.util.FileUtils;

@Service
public class NoticeServiceImpl implements NoticeService{

	@Autowired
	private NoticeMapper mapper;
	
	@Resource(name="fileUtils")
	private FileUtils fileUtils;
	
	// 공지사항 등록
	@Override
	public int notregistr(NoticeVO notice,MultipartHttpServletRequest notRequest) throws Exception {
		 int result =   mapper.notregistr(notice);
	  
	        // 파일이 첨부된 경우에만 파일 정보를 처리합니다.
	        List<Map<String, Object>> list = fileUtils.parseInsertFileInfo(notice, notRequest);
	        int size = list.size();
	        for(int i = 0; i < size; i++) {
	            mapper.insertFile(list.get(i));
	        }
	    return result;
	}
	
	// 공지사항 목록
	@Override
	public List<NoticeVO> getlist() {
		return mapper.getlist();
	}
	
	// 공지사항 목록(페이징 적용)
	@Override
	public List<NoticeVO> getListPaging(NotCriteria ncri) {
		return mapper.getListPaging(ncri);
	}

	// 공지사항 조회
	@Override
	public NoticeVO getPage(String NOTICE_ID) {
		return mapper.getPage(NOTICE_ID);
	}

	// 공지사항 수정
	@Override
	public int notmodify(NoticeVO notice) throws Exception {
	    
	    // 파일 삭제 여부와 관계없이 게시글 정보만 업데이트
	    return mapper.notmodify(notice);
	}

	// 공지사항 삭제
	@Override
	public int notdelete(String NOTICE_ID) {
		return mapper.notdelete(NOTICE_ID);
	}

	// 공지사항 총 갯수
	@Override
	public int getTotal(NotCriteria ncri) {
		return mapper.getTotal(ncri);
	}

	// 공지사항 조회수
	@Override
	public int noticeViews(String NOTICE_ID) {
		return mapper.noticeViews(NOTICE_ID);
	}

	// 공지사항 첨부파일 조회
	@Override
	public List<FilesVO> selectFileList(String NOTICE_ID) {
		return mapper.selectFileList(NOTICE_ID);
	}

	// 공지사항 첨부파일 다운로드
	@Override
	public Map<String, Object> selectFileInfo(Map<String, Object> map)throws Exception {
		return mapper.selectFileInfo(map);
	}

	// 공지사항 첨부파일 삭제
	@Override
	public int deleteFile(String NOTICE_ID) throws Exception{
		return mapper.deleteFile(NOTICE_ID);
	}

	
	// 공지사항 회원이름
	@Override
	public MemberVO member(String MEMBER_ID) {
		return mapper.member(MEMBER_ID);
	}

	

	

	// 공지사항 파일업로드 수정
//	@Override
//	public List<FilesVO> updateFileList(int NOTICE_ID) {
//		return mapper.updateFileList(NOTICE_ID);
//	}

}
