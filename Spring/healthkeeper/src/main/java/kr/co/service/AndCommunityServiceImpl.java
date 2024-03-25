package kr.co.service;

import kr.co.and.AndCategoryVO;
import kr.co.and.AndFaqVO;
import kr.co.and.AndNoticeVO;
import kr.co.and.AndQuestionVO;
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
    public List<AndQuestionVO> list(String number) {
        return mapper.list(number);
    }

    @Override
    public List<AndFaqVO> faq() {
        return mapper.faq();
    }

    @Override
    public List<AndNoticeVO> notice() {
        return mapper.notice();
    }

    @Override
    public List<AndCategoryVO> category() {
        return mapper.category();
    }

    @Override
    public int newWrite(AndQuestionVO vo) {
        return mapper.newWrite(vo);
    }

}
