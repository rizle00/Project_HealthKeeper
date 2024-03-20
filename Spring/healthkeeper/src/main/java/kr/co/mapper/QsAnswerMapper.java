package kr.co.mapper;

import java.util.List;

import kr.co.model.QsAnswerVO;

public interface QsAnswerMapper {
	
	// 답글 조회
	public List<QsAnswerVO> readAnswer(int QUE_ID);
	
	// 답글 작성
	public void writeAnswer(QsAnswerVO anvo);
	
	// 답글 삭제
	
}
