package kr.co.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.mapper.MemberMapper;
import kr.co.model.MemberVO;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberMapper mapper;
	
	// 회원가입
	@Override
	public void memberjoin(MemberVO member) throws Exception{
		mapper.memberjoin(member);
	}

	// 로그인
	@Override
	public MemberVO memberlogin(MemberVO member) throws Exception {
		return mapper.memberlogin(member);
	}

	// 아이디 중복검사
	@Override
	public int idchk(String email) throws Exception {
		return mapper.idchk(email);
	}

	public int socialCheck(String social){
		return mapper.socialCheck(social);
	}

	public MemberVO socialLogin(String social){
		return mapper.socialLogin(social);
	}

	@Override
	public int checkDuplicateEmail(String email) {
		return mapper.checkDuplicateEmail(email);
	}
}
