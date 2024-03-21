package kr.co.model;

import java.util.Date;

import lombok.Data;

@Data
public class QsAnswerVO {
	
	// 댓글 id
	private int ANS_ID;
	
	// 댓글 내용
	private String CONTENT;
	
	// 질문게시판 id
	private int QUE_ID;
	
	// 관리자 id
	private int ADMIN_ID;
	
	// 댓글 등록날짜
	private Date TIME;

}
