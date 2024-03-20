package kr.co.service;

import kr.co.mapper.AndMemberMapper;
import kr.co.mapper.FaqMapper;
import kr.co.model.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
@Service
public class AndMemberServiceImpl implements AndMemberService {

    @Autowired
    private AndMemberMapper mapper;
    @Override
    public int insert(HashMap<String, Object> map) {
        return mapper.insert(map);
    }

    @Override
    public MemberVO login(String email) {
        return mapper.login(email);
    }

    @Override
    public String idcheck(String member_id) {
        return mapper.idcheck(member_id);
    }

    @Override
    public void join(MemberVO vo) {
        mapper.join(vo);
    }

    @Override
    public String findid(MemberVO vo) {
        return mapper.findid(vo);
    }

    @Override
    public String findpw(MemberVO vo) {
        return mapper.findpw(vo);
    }

    @Override
    public String socialIdCheck(String social) {
        return mapper.socialIdCheck(social);
    }

    @Override
    public MemberVO socialLogin(String social) {
        return mapper.socialLogin(social);
    }

    @Override
    public String partnerCheck(String partner_id) {
        return mapper.partnerCheck(partner_id);
    }

    @Override
    public int modify(MemberVO vo) {
        return  mapper.modify(vo);
    }

    @Override
    public void patientRegister(MemberVO vo) {
        mapper.patientRegister(vo);
    }

    @Override
    public int resetpw(MemberVO vo) {
        return mapper.resetpw(vo);
    }
}
