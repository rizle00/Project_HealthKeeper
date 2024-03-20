package kr.co.service;

import kr.co.model.MemberVO;

public interface MemberService {

	// 회원가입
	public void memberjoin(MemberVO member) throws Exception;
	
	// 로그인
	public MemberVO memberlogin(MemberVO member) throws Exception;
	
	// 아이디 확인
	public int idchk(String email) throws Exception;
}
