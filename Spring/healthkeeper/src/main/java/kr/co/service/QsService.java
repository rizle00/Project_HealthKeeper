package kr.co.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.model.CateGoryVO;
import kr.co.model.FilesVO;
import kr.co.model.MemberVO;
import kr.co.model.QsCriteria;
import kr.co.model.QsVO;

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
	public QsVO getpage(int QUE_ID);
	
	// 게시판 수정
	public int update(QsVO qs);
	
	// 게시판 삭제
	public int delete(int QUE_ID);
	
	// 게시판 조회수
	public int qsViews(int QUE_ID);
	
	// 카테고리 리스트
	public List<CateGoryVO> catelist() throws Exception;
	
	public CateGoryVO cate(int CATEGORY_ID);
	
	
	// 게시판 첨부파일 조회
	public List<FilesVO> fileList(int QUE_ID);
	
	// 게시판 첨부파일 다운로드
	public Map<String, Object> filedown(Map<String, Object> map) throws Exception;

}
