package kr.co.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.model.FilesVO;
import kr.co.model.NotCriteria;
import kr.co.model.NoticeVO;

public interface NoticeService {
	
	// 공지사항 등록
	public void notregistr(NoticeVO notice) throws Exception;

	// 공지사항 목록
	public List<NoticeVO> getlist();
	
	// 공지사항 목록(페이징 적용)
	public List<NoticeVO> getListPaging(NotCriteria ncri);
	
	// 공지사항 조회
	public NoticeVO getPage(int NOTICE_ID);
	
	// 공지사항 수정
	public int notmodify(NoticeVO notice, 
						 String[] files, 
						 String[] fileNames,
						 MultipartHttpServletRequest notRequest) throws Exception;
	
	// 공지사항 삭제
	public int notdelete(int NOTICE_ID);
	
	// 공지사항 총 갯수
	public int getTotal(NotCriteria ncri);
	
	// 공지사항 조회수
	public int noticeViews(int NOTICE_ID);
	
	// 첨부파일 조회
	//public List<FilesVO> selectFileList(int NOTICE_ID);
	
	// 첨부파일 수정조회
	//public List<FilesVO> updateFileList(int NOTICE_ID);
	
	// 첨부파일 다운로드
	//public Map<String,Object> selectFileInfo(Map<String, Object> map) throws Exception;
	
	// 첨부파일 삭제
	//public int deleteFile(int NOTICE_ID) throws Exception;; 
	
	
}
