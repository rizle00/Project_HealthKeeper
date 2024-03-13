package kr.co.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.mapper.FaqMapper;
import kr.co.model.FaqVO;

@Service
public class FaqServiceImpl implements FaqService{
	
	@Autowired
	private FaqMapper mapper;

	// FAQ 게시판 등록
	@Override
	public void faqregistr(FaqVO faqvo) {
		mapper.faqregistr(faqvo);
	}

	// FAQ 게시판 목록
	@Override
	public List<FaqVO> faqlist() {
		return mapper.faqlist();
	}

	
	// FAQ 게시판 수정
	@Override
	public int faqupdate(FaqVO faqvo) {
		return mapper.faqupdate(faqvo);
	}


}
