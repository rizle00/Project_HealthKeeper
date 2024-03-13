package kr.co.model;

import lombok.Data;

@Data
public class FaqVO {

	// FAQ id
	private int FAQ_ID;
	
	// FAQ 게시판 제목
	private String TITLE;
	
	// FAQ 게시판 내용
	private String CONTENT;
	
	// FAQ 게시판 작성자 ( 관리자 등급 )
	private MemberVO member;
}
