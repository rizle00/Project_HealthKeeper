package kr.co.model;

import java.util.Date;

import lombok.Data;

@Data
public class QsReplyVO {
	
	// 게시글? 번호
	private int QS_BNO;
	
	// 댓글 번호
	private int QRNO;
	
	// 댓글 내용
	private String CONTENT;
	
	// 댓글 작성자
	private String WRITER;
	
	// 댓글 작성일자
	private Date REGDATE;
	

	
}
