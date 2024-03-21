package kr.co.service;

import java.util.List;

import kr.co.model.FaqCriteria;
import kr.co.model.FaqVO;

public interface FaqService {
	
	// FAQ 게시판 등록
	public void faqregistr(FaqVO faq);
	
	// FAQ 게시판 목록
	public List<FaqVO> faqlist();
	
	// FAQ 게시판 목록 (페이징 적용)
	public List<FaqVO> faqlistPaging(FaqCriteria fcri);
	
	// FAQ 게시판 조회
	public FaqVO faqpage(String FAQ_ID);
	
	// FAQ 게시판 수정
	public int faqupdate(FaqVO faq);
	
	// FAQ 게시판 삭제
	public int faqdelete(String FAQ_ID);
	
	// FAQ 게시글 총 갯수
	public int faqTotal();

}
