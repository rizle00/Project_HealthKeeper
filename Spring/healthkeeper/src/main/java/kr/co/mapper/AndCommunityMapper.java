package kr.co.mapper;

import kr.co.model.*;

import java.util.List;

public interface AndCommunityMapper {


    public List<QsVO> list(String number);

    public List<FaqVO> faq();

    public List<NoticeVO> notice();

    public List<CateGoryVO> category();

    public int newWrite(QsVO vo);
}
