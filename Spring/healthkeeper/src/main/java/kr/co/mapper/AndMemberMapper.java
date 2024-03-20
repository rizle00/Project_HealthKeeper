package kr.co.mapper;

import kr.co.model.MemberVO;

import java.util.HashMap;

public interface AndMemberMapper {
    public int insert(HashMap<String, Object> map);

    public MemberVO login(String email);

    public String idcheck(String member_id);

    public void join(MemberVO vo);

    public String findid(MemberVO vo);

    public String findpw(MemberVO vo);

    public String socialIdCheck(String social);

    public MemberVO socialLogin(String social);

    public String partnerCheck(String partner_id);

    public int modify(MemberVO vo);

    public void patientRegister(MemberVO vo);

    public int resetpw(MemberVO vo);
}
