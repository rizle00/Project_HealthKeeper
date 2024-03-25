package kr.co.mapper;

import java.util.List;
import java.util.Map;

import kr.co.model.*;
import kr.co.model.CateGoryVO;
import kr.co.model.FilesVO;
import kr.co.model.MemberVO;
import kr.co.model.QsCriteria;
import kr.co.model.QsVO;

public interface QsMapper {
	
	// 게시판 등록
	public void qsregistr(QsVO qs);
	
	// 게시판 목록
	public List<QsVO> getlist();
	
	// 게시판 목록(페이징 적용처리)
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
	
	// 게시판 카테고리
	public List<CateGoryVO> catelist();
	
	// 게시판 카테고리 id
	public CateGoryVO cate(String CATEGORY_ID);
	

	
	// 게시판 파일첨부
	public void insertfile(Map<String, Object> map) throws Exception;
	
	// 게시판 첨부파일 조회
	public List<FilesVO> fileList(String QUE_ID);
	
	// 게시판 첨부파일 다운로드
	public Map<String, Object> filedown(Map<String, Object> map);

    public MemberVO selectMember(String QUE_ID);

	public QsVO selectQue(String QUE_ID);
}
