package kr.co.mapper;

import kr.co.model.MemberVO;

public interface MemberMapper {

	// 회원가입
	public void memberjoin(MemberVO member);
	
	// 로그인
	public MemberVO memberlogin(MemberVO member);
}
