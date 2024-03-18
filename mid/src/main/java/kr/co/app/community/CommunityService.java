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

	public List<QueVO>  list(){
		return sql.selectList("comm.que");
	}
	public List<FaqVO>  faq(){
		return sql.selectList("comm.faq");
	}
	public List<NoticeVO> notice(){
		return sql.selectList("comm.notice");
	}
	
	
}
