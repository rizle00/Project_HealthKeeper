package kr.co.app.member;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;



@Service
public class MemberService {
	@Autowired
	@Qualifier("hanul")
	SqlSession sql;
	
	public MemberVO login(String user_id) {
		return sql.selectOne("me.login", user_id);
	}
	public MemberVO guardian(String user_id) {
		return sql.selectOne("me.guardian", user_id);
	}

	public String idcheck(String guardian_id) {
		return sql.selectOne("me.idcheck",guardian_id);
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
}
