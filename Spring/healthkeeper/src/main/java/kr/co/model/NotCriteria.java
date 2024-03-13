package kr.co.model;

import lombok.Data;

@Data
public class NotCriteria {
	
	// 현재 페이지
    private int pageNum;
    
    // 한 페이지 당 보여질 게시물 갯수
    private int amount;
    
    // 검색키워드
    private String keyword;
    
    // 검색타입
    private String type;
    
    // 검색타입 배열 변환
    private String[] typeArr;
    
    //pageNum = 1, amount = 10
    public NotCriteria() {
        this(1,10);
    }
    
    // 생성자 => 원하는 pageNum, 원하는 amount
    public NotCriteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }
    
    public void setType(String type) {
    	this.type = type;
    	this.typeArr = type.split("");
    }

}
