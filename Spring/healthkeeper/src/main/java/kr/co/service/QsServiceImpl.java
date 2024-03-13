package kr.co.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.mapper.QsMapper;
import kr.co.model.QsCriteria;
import kr.co.model.QsVO;

@Service
public class QsServiceImpl implements QsService{
	
	@Autowired
	private QsMapper mapper;

	// 게시판 등록
	@Override
	public void qsregistr(QsVO qs) {
		mapper.qsregistr(qs);
	}

	// 게시판 목록
	@Override
	public List<QsVO> getlist() {
		return mapper.getlist();
	}
	
	// 게시판 목록(페이징 처리)
	@Override
	public List<QsVO> getlistPaging(QsCriteria qcri) {
		return mapper.getlistPaging(qcri);
	}
	
	// 게시판 글 총 갯수
	@Override
	public int getTotal(QsCriteria qcri) {
		return mapper.getTotal(qcri);
	}

	// 게시판 조회
	@Override
	public QsVO getpage(int QS_BNO) {
		return mapper.getpage(QS_BNO);
	}

	// 게시판 수정
	@Override
	public int update(QsVO qs) {
		return mapper.update(qs);
	}

	// 게시판 삭제
	@Override
	public int delete(int QS_BNO) {
		return mapper.delete(QS_BNO);
	}

	// 게시판 조회수
	@Override
	public int qsViews(int QS_BNO) {
		return mapper.qsViews(QS_BNO);
	}

}
