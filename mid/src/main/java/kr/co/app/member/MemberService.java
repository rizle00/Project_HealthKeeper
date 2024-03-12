package kr.co.app.member;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;


@Service
public class MemberService {
	@Autowired
	@Qualifier("hanul")
	SqlSession sql;

	public int insert(HashMap<String, Object> map){
		return sql.insert("test.insert", map);
	}
	
	public MemberVO login(String member_id) {
		return sql.selectOne("me.login", member_id);
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
}
