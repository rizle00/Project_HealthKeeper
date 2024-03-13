package kr.co.mapper;

import java.util.List;

import kr.co.model.FaqVO;

public interface FaqMapper {
	
	// FAQ 게시판 등록
	public void faqregistr(FaqVO faqvo);
	
	// FAQ 게시판 목록
	public List<FaqVO> faqlist();
	
	
	
	// FAQ 게시판 수정
	public int faqupdate(FaqVO faqvo);

}
