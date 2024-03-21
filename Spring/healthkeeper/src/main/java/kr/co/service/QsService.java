package kr.co.service;

import java.util.List;
import java.util.Map;

import kr.co.model.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface QsService {
	
	// 게시판 등록
	public void qsregistr(QsVO qs, MultipartHttpServletRequest qsRequest) throws Exception;
	
	// 게시판 목록
	public List<QsVO> getlist();
	
	// 게시판 목록(페이징 처리)
	public List<QsVO> getlistPaging(QsCriteria qcri);
	
	// 게시판 총 갯수
	public int getTotal(QsCriteria qcri);
	
	// 게시판 조회
	public QsVO getpage(String QUE_ID);
	
	// 게시판 수정
	public int update(QsVO qs);
	
	// 게시판 삭제
	public int delete(String QUE_ID);
	
	// 게시판 조회수
	public int qsViews(String QUE_ID);
	
	// 카테고리 리스트
	public List<CateGoryVO> catelist() throws Exception;
	
	public CateGoryVO cate(String CATEGORY_ID);
	
	// 게시판 첨부파일 조회
	public List<FilesVO> fileList(String QUE_ID);
	
	// 게시판 첨부파일 다운로드
	public Map<String, Object> filedown(Map<String, Object> map) throws Exception;

   public MemberVO selectMember(String QUE_ID);

	public QsVO selectQue(String QUE_ID);
}
