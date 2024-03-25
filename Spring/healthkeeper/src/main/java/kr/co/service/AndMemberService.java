package kr.co.service;

import kr.co.and.AndMemberVO;
import kr.co.and.firebase.RequestDTO;
import kr.co.and.firebase.TypeVO;
import kr.co.model.*;

import java.util.HashMap;
import java.util.List;

public interface AndMemberService {

    public int insertCondition(ConditionVO vo);

    public AndMemberVO login(String email);

    public Integer idcheck(String email) ;
    public Integer join(AndMemberVO vo);

    public String findid(AndMemberVO vo);
    public int findpw(AndMemberVO vo) ;

    public int socialIdCheck(String social);

    public AndMemberVO socialLogin(String social) ;

    public String partnerCheck(String email) ;

    public int modify(AndMemberVO vo);

    public int patientRegister(HashMap<String, String> map) ;

    public int resetpw(AndMemberVO vo) ;
    public AndMemberVO guardian(String user_id) ;
    public List<HospitalVO> hospitalList(String name);

    public List<String> doctorsList(MemberHospitalVO vo);

    public HashMap<String, Object> condition(String id);

    public TypeVO type(String category_id);

    public int insertAlarm(RequestDTO dto);
    public int insertAlarmG(RequestDTO dto);

    public Integer insertDisease(DiseaseVO vo);

    public int updateToken(HashMap<String, String> map);

    public List<AndMemberVO> memberList();

    public List<AlarmLogVO> alarmLog(String params);

   public int updateAlarm(HashMap<String, String> map);
}
