package kr.co.service;

import kr.co.mapper.AndCommunityMapper;
import kr.co.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AndCommunityServiceImpl implements AndCommunityService{

    @Autowired
    private AndCommunityMapper mapper;

    @Override
    public List<QsVO> list(String number) {
        return mapper.list(number);
    }

    @Override
    public List<FaqVO> faq() {
        return mapper.faq();
    }

    @Override
    public List<NoticeVO> notice() {
        return mapper.notice();
    }

    @Override
    public List<CateGoryVO> category() {
        return mapper.category();
    }

    @Override
    public int newWrite(QsVO vo) {
        return mapper.newWrite(vo);
    }

}
