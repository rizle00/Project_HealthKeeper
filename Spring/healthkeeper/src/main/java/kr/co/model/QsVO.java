package kr.co.model;

import java.util.Date;
import lombok.Data;

@Data
public class QsVO {
	
	// 게시판 번호
	private int QUE_ID;
		
	// 게시판 제목
	private String TITLE;
		
	// 게시판 내용
	private String CONTENT;
		
	// 게시판 작성일자
	private Date TIME;
		
	// 게시판 작성자
	private int MEMBER_ID;
	
	// 게시판 카테고리?
	private int CATEGORY_ID;
		
	// 게시판 조회수
	private int READ_CNT;

}
