package kr.co.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.mapper.QsAnswerMapper;
import kr.co.model.QsAnswerVO;

@Service
public class QsAnswerServiceImpl implements QsAnswerService{

	@Autowired
	private QsAnswerMapper mapper;
	
	// 답글 조회
	@Override
	public List<QsAnswerVO> readAnswer(int QUE_ID) {
		return mapper.readAnswer(QUE_ID);
	}

	// 답글 작성
	@Override
	public void writeAnswer(QsAnswerVO anvo) {
		mapper.writeAnswer(anvo);
	}

}
