package kr.co.service;

import kr.co.and.AndMemberVO;
import kr.co.and.firebase.RequestDTO;
import kr.co.and.firebase.TypeVO;
import kr.co.mapper.AndMemberMapper;
import kr.co.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AndMemberServiceImpl implements AndMemberService {

    @Autowired
    private AndMemberMapper mapper;
    @Override
    public int insertCondition(ConditionVO vo) {
        return mapper.insertCondition(vo);
    }

    @Override
    public AndMemberVO login(String email) {
        return mapper.login(email);
    }

    @Override
    public Integer idcheck(String email) {
        return mapper.idcheck(email);
    }

    @Override
    public Integer join(AndMemberVO vo) {
        return mapper.join(vo);
    }

    @Override
    public String findid(AndMemberVO vo) {
        return mapper.findid(vo);
    }

    @Override
    public int findpw(AndMemberVO vo) {
        return mapper.findpw(vo);
    }

    @Override
    public int socialIdCheck(String social) {
        return mapper.socialIdCheck(social);
    }

    @Override
    public AndMemberVO socialLogin(String social) {
        return mapper.socialLogin(social);
    }

    @Override
    public String partnerCheck(String email) {
        return mapper.partnerCheck(email);
    }

    @Override
    public int modify(AndMemberVO vo) {
        return  mapper.modify(vo);
    }

    @Override
    public int patientRegister(HashMap<String, String> map) {
       return mapper.patientRegister(map);
    }

    @Override
    public int resetpw(AndMemberVO vo) {
        return mapper.resetpw(vo);
    }

    @Override
    public AndMemberVO guardian(String user_id) {
        return mapper.guardian(user_id);
    }

    @Override
    public List<HospitalVO> hospitalList(String name) {
        return mapper.hospitalList(name);
    }

    @Override
    public List<String> doctorsList(MemberHospitalVO vo) {
        return mapper.doctorsList(vo);
    }

    @Override
    public HashMap<String, Object> condition(String id) {
        return mapper.condition(id);
    }

    @Override
    public TypeVO type(String category_id) {
        return mapper.type(category_id);
    }

    @Override
    public int insertAlarm(RequestDTO dto) {
        return mapper.insertAlarm(dto);
    }
    public int insertAlarmG(RequestDTO dto) {
        return mapper.insertAlarmG(dto);
    }

    @Override
    public Integer insertDisease(DiseaseVO vo) {
        return mapper.insertDisease(vo);
    }

    @Override
    public int updateToken(HashMap<String, String> map) {
        return mapper.updateToken(map);
    }

    @Override
    public List<AndMemberVO> memberList() {
        return mapper.memberList();
    }

    @Override
    public List<AlarmLogVO> alarmLog(String params) {
        return mapper.alarmLog(params);
    }

    @Override
    public int updateAlarm(HashMap<String, String> map) {
        return mapper.updateAlarm(map);
    }
}
