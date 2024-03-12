package kr.co.app.member;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;



@Service
public class MemberService {
	@Autowired
	@Qualifier("hanul")
	SqlSession sql;
	
	public MemberVO login(String email) {
		return sql.selectOne("me.login", email);
	}
	
	public String idcheck(String member_id) {
		return sql.selectOne("me.idcheck",member_id);
	}
	
	public void join(MemberVO vo) {
		sql.insert("me.join",vo);
	}
	
	public String findid(MemberVO vo) {
		return sql.selectOne("me.findid",vo);
	}
	
	public int resetpw(MemberVO vo) {
		return sql.update("me.resetPassword",vo);
	}
	
	public String socialIdCheck(String social) {
		return sql.selectOne("me.socialCheck",social);
	}
	
	public MemberVO socialLogin(String social) {
		return sql.selectOne("me.socialLogin",social);
	}
	
	public String partnerCheck(String partner_id) {
		return sql.selectOne("me.partnerCheck",partner_id);
	}

	public int modify(MemberVO vo){
		return 0;
	}
	
	public void patientRegister(MemberVO vo) {
		sql.update("me.patientRegister",vo);
	}
}
