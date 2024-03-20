package kr.co.mapper;

import kr.co.model.MemberVO;

public interface MemberMapper {

	// 회원가입
	public void memberjoin(MemberVO member);
	
	// 로그인
	public MemberVO memberlogin(MemberVO member);
	
	// 아이디 확인
	public int idchk(String email);

	public int socialCheck(String social);

	public MemberVO socialLogin(String social);
	
	public int checkDuplicateEmail(String email);
}
