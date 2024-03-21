package kr.co.service;

import kr.co.and.firebase.RequestDTO;
import kr.co.and.firebase.TypeVO;
import kr.co.model.DiseaseVO;
import kr.co.model.HospitalVO;
import kr.co.model.MemberHospitalVO;
import kr.co.model.MemberVO;

import java.util.HashMap;
import java.util.List;

public interface AndMemberService {

    public int insert(HashMap<String, Object> map);

    public MemberVO login(String email);

    public String idcheck(String member_id) ;
    public void join(MemberVO vo);

    public String findid(MemberVO vo);
    public String findpw(MemberVO vo) ;

    public String socialIdCheck(String social);

    public MemberVO socialLogin(String social) ;

    public String partnerCheck(String partner_id) ;

    public int modify(MemberVO vo);

    public void patientRegister(MemberVO vo) ;

    public int resetpw(MemberVO vo) ;
    public MemberVO guardian(String user_id) ;
    public List<HospitalVO> hospitalList(String name);

    public List<String> doctorsList(MemberHospitalVO vo);

    public HashMap<String, Object> condition(String id);

    public TypeVO type(String category_id);

    public int insertAlarm(RequestDTO dto);

    public Integer insertDisease(DiseaseVO vo);
}
