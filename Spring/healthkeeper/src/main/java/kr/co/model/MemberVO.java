package kr.co.model;


import lombok.Data;

@Data
public class MemberVO {
	
	// 회원 아이디
	private int MEMBER_ID;
	
	// 보호자 아이디
    private int GUARDIAN_ID;
    
    // 회원 비밀번호
    private String PW;
    
    // 회원 이름
    private String NAME;
    
    // 회원 전화번호
    private String PHONE;
    
    // 회원 생년월일
    private String BIRTH;
    
    // 회원 성별
    private String GENDER;
    
    // 회원 혈액형
    private String BLOOD;
    
    // 회원 소셜로그인
    private String SOCIAL;
    
    // 회원 이메일
    private String EMAIL;
    
    // 회원 기본주소
    private String ADDRESS;
    
    // 회원 상세주소
    private String ADDRESS_DETAIL;
    
    // 회원 알람여부
    private String ALARM;
    
    // 회원 등급 ( 일반 , 관리자 )
    private String ROLE;

}
