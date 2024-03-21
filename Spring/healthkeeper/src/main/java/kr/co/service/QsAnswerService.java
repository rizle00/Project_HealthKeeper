package kr.co.service;

import java.util.List;

import kr.co.model.QsAnswerVO;

public interface QsAnswerService {
	
	// 답글 조회
	public List<QsAnswerVO> readAnswer(String QUE_ID);
	
	// 답글 작성
	public int writeAnswer(QsAnswerVO anvo);

}
