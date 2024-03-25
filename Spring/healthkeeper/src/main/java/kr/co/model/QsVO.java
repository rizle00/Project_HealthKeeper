package kr.co.model;

import java.util.Date;
import lombok.Data;

@Data
public class QsVO {
	
	// 게시판 번호
	private String QUE_ID;
		
	// 게시판 제목
	private String TITLE;
	
	private String NAME;
	
	private String CATEGORY_NAME;
		
	// 게시판 내용
	private String CONTENT;
		
	// 게시판 작성일자
	private Date TIME;
		
	// 게시판 작성자
	private String MEMBER_ID;
	
	// 게시판 카테고리
	private String CATEGORY_ID;
	
	private CateGoryVO category; // CategoryVO 객체
	
		
	// 게시판 조회수
	private int READ_CNT;
	private QsAnswerVO answer;
	private String Name, SECRET;

}
