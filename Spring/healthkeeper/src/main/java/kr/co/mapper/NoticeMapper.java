package kr.co.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.model.FilesVO;
import kr.co.model.NotCriteria;
import kr.co.model.NoticeVO;

public interface NoticeMapper {
	
	// 공지사항 등록
	public void notregistr(NoticeVO notice);
	
	// 공지사항 목록
	public List<NoticeVO> getlist();
	
	// 공지사항 목록(페이징 적용)
	public List<NoticeVO> getListPaging(NotCriteria ncri);
	
	// 공지사항 조회
	public NoticeVO getPage(int NOTICE_ID);
	
	// 공지사항 수정
	public int notmodify(NoticeVO notice);
	
	// 공지사항 삭제
	public int notdelete(int NOTICE_ID);
	
	// 공지사항 총 갯수
	public int getTotal(NotCriteria ncri);
	
	// 공지사항 조회수
	public int noticeViews(int NOTICE_ID);
	
	// 공지사항 파일첨부
	//public void insertFile(Map<String, Object> map) throws Exception;
	
	//public void updateinsertFile(Map<String, Object> map) throws Exception;
	
	// 공지사항 첨부파일 조회
	//public List<FilesVO> selectFileList(int NOTICE_ID);
	
	// 공지사항 수정업로드 조회
	//public List<FilesVO> updateFileList(int NOTICE_ID);
	
	// 추가된 파일 목록 조회 메서드 추가
    //public List<FilesVO> getAddedFileList(int NOTICE_ID);
	
	// 공지사항 첨부파일 다운로드
	//public Map<String,Object> selectFileInfo(Map<String, Object> map)throws Exception;
	
	// 공지사항 첨부파일 삭제
	//public int deleteFile(int NOTICE_ID) throws Exception;
	
	// 공지사항 수정 시 파일업로드
	//public void updateFile(HashMap<String, Object> map);
}
