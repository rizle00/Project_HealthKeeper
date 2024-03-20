package kr.co.model;

import lombok.Data;

@Data
public class FaqCriteria {
	
	// 현재 페이지
	private int pageNum;
	
	// 한 페이지 당 보여질 게시물 갯수
	private int amount;
	
	//pageNum = 1, amount = 9
    public FaqCriteria() {
        this(1,6);
    }
    
 // 생성자 => 원하는 pageNum, 원하는 amount
    public FaqCriteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }

}
