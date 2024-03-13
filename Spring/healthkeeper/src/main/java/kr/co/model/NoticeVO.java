package kr.co.model;

import java.util.Date;

import lombok.Data;

@Data
public class NoticeVO {
	
	// 공지사항 번호
	private int NOTICE_ID;
	
	// 공지사항 제목
	private String TITLE;
	
	// 공지사항 내용
	private String CONTENT;
	
	// 공지사항 작성일자
	private Date TIME;
	
	// 공지사항 작성자
	private int MEMBER_ID;
	
	// 공지사항 조회수
	private int READ_CNT;
	
	
}
