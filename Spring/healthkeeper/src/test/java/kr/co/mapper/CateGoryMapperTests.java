package kr.co.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class CateGoryMapperTests {
	
	private static final Logger log = LoggerFactory.getLogger(CateGoryMapperTests.class);
	
//	@Autowired
//	private CateGoryMapper mapper;
//	
//	// 카테고리 리스트 테스트
//	@Test
//	public void catelist() {
//		System.out.println("catelist()......" + mapper.catelist());
//	}

}
