package kr.co.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.mapper.FaqMapper;
import kr.co.model.FaqCriteria;
import kr.co.model.FaqVO;

@Service
public class FaqServiceImpl implements FaqService{

	@Autowired
	private FaqMapper mapper;

	// FAQ 게시판 등록
	@Override
	public void faqregistr(FaqVO faq) {
		mapper.faqregistr(faq);
	}

	// FAQ 게시판 목록
	@Override
	public List<FaqVO> faqlist() {
		return mapper.faqlist();
	}

	// FAQ 게시판 조회
	@Override
	public FaqVO faqpage(int FAQ_ID) {
		return mapper.faqpage(FAQ_ID);
	}
	
	// FAQ 게시판 수정
	@Override
	public int faqupdate(FaqVO faq) {
		return mapper.faqupdate(faq);
	}
	
	// FAQ 게시판 삭제
	@Override
	public int faqdelete(int FAQ_ID) {
		return mapper.faqdelete(FAQ_ID);
	}

	// FAQ 목록 페이징 처리
	@Override
	public List<FaqVO> faqlistPaging(FaqCriteria fcri) {
		return mapper.faqlistPaging(fcri);
	}

	// FAQ 게시글 총 갯수
	@Override
	public int faqTotal() {
		return mapper.faqTotal();
	}

}
