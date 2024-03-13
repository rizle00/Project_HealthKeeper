package kr.co.mapper;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.co.model.MemberVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class MemberMapperTests {
	
	private static final Logger log = LoggerFactory.getLogger(MemberMapperTests.class);
	
	@Autowired
	private MemberMapper mapper;
	
	// 회원가입 테스트
//	@Test
//	public void memberjoin() {
//		MemberVO member = new MemberVO();
//		
//		member.setMEMBER_ID(1);
//		member.setGUARDIAN_ID(2);
//		member.setPW("mapper");
//		member.setNAME("mapper test");
//		member.setPHONE("010-9595-9595");
//		member.setBIRTH(new Date(00,01,01));
//		member.setGENDER("남");
//		member.setBLOOD("A");
//		member.setSOCIAL("x");
//		member.setEMAIL("mappertest@naver.com");
//		member.setADDRESS("광주 서구 농성동");
//		member.setADDRESS_DETAIL("member test 중");
//		member.setALARM("x");
//		member.setROLE("m");
//		
//		mapper.memberjoin(member);
//	}
	
	// 로그인 테스트
	@Test
	public void memberlogin() {
		
		MemberVO member = new MemberVO();
		
		member.setEMAIL("bb3@bb.com");
		member.setPW("13");
		
		mapper.memberlogin(member);
		System.out.println("결과 값 : " + mapper.memberlogin(member));
		
	}
}
