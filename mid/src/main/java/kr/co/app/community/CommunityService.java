package kr.co.app.community;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Objects;


@Service
public class CommunityService {
	@Autowired
	@Qualifier("hanul")
	SqlSession sql;
	public List<AnswerVO> answer(String params){
		return sql.selectList("comm.answer", params);
	}

	
	  public List<QueVO> que4(){ 
		  return sql.selectList("comm.que4"); }
	 
	public List<QueVO>  list(String number){
		return sql.selectList("comm.question", number);
	}
	public List<FaqVO>  faq(){
		return sql.selectList("comm.faq");
	}
	public List<NoticeVO> notice(){
		return sql.selectList("comm.notice");
	}
	public List<CategoryVO> category(){
		return sql.selectList("comm.category");
	}

	public int newWrite(QueVO vo) {
		
		return sql.insert("comm.newWrite",  vo);
	}
	
	
	
}
