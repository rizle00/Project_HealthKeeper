package kr.co.mapper;

import kr.co.and.AndCategoryVO;
import kr.co.and.AndFaqVO;
import kr.co.and.AndNoticeVO;
import kr.co.and.AndQuestionVO;
import kr.co.model.*;

import java.util.List;

public interface AndCommunityMapper {


    public List<AndQuestionVO> list(String number);

    public List<AndFaqVO> faq();

    public List<AndNoticeVO> notice();

    public List<AndCategoryVO> category();

    public int newWrite(AndQuestionVO vo);
}
