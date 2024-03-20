package kr.co.model;

import java.util.Date;

import lombok.Data;

@Data
public class FilesVO {
	
	// 파일 id
	private int FILE_ID;
	
	// 파일 이름
	private String NAME;
	
	// 파일 경로
	private String PATH;
	
	// 파일 등록일
	private Date TIME;
	
}
