package kr.co.model;

import lombok.Data;

@Data
public class QsCriteria {
	
	// 현재 페이지
	private int pageNum;
	
	// 한 페이지 당 보여질 게시글 갯수
	private int amount;
	
	// 검색키워드
	private String Keyword;
	
	// 검색타입
	private String type;
	
	// 검색타입 배열 변환
	private String[] typeArr;
	
	// pageNum = 1, amount = 10
	// 파라미터 없이 QsCriteria 클래스를 호출하였을 때는 기본적으로 pageNum은 1, amount는 10을 가지도록 생성자를 작성
	public QsCriteria() {
		this(1,10);
	}
	
	// 생성자 -> 원하는 pageNum , 원하는 amount
	// 파라미터와 함께 QsCriteria 를 호출하게 되면 파라미터의 값이 각각 pageNum과 amount 값에 저장되도록 생성자를 작성
	public QsCriteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	//'type' 문자열을 각 문자 단위로 분리하여 'typeArr' 배열에 저장하는 역할
	// 이 코드는 주로 문자열을 처리하고, 각 문자에 대한 작업을 할 때 사용될 수 있습니다.
	public void setType(String type) {
    	this.type = type;
    	this.typeArr = type.split("");
    }

}
