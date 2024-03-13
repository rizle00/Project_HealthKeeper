package kr.co.model;

import java.util.Date;

import lombok.Data;

@Data
public class FilesVO {
	
	// 파일 id
	private int FILE_ID;
	
	// 파일 이름
	private String FILE_NAME;
	
	// 파일 경로
	private String FILE_PATH;
	
	// 파일 크기
	private long FILE_SIZE;
	
	// 파일 등록일
	private Date FILE_REGDATE;
}
