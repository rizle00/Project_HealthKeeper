package kr.co.service;

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
public class MemberServiceTests {
	
	private static final Logger log = LoggerFactory.getLogger(MemberServiceTests.class);
	
	@Autowired
	private MemberService service;
	
	// 회원가입 테스트
//	@Test
//	public void memberjoin() throws Exception{
//		MemberVO member = new MemberVO();
//		
//		member.setMEMBER_ID(1);
//		member.setGUARDIAN_ID(2);
//		member.setPW("service");
//		member.setNAME("service test");
//		member.setPHONE("010-9595-9595");
//		member.setBIRTH(new Date(00,01,01));
//		member.setGENDER("남");
//		member.setBLOOD("A");
//		member.setSOCIAL("x");
//		member.setEMAIL("servicetest@naver.com");
//		member.setADDRESS("광주 서구 농성동");
//		member.setADDRESS_DETAIL("service test 중");
//		member.setALARM("x");
//		member.setROLE("m");
//		
//		service.memberjoin(member);
//	}

	// 로그인 테스트
	@Test
	public void login() throws Exception{
		MemberVO vo = new MemberVO();
		
		vo.setEMAIL("bb@bb.com");
		vo.setPW("1");
		
		service.memberlogin(vo);
		System.out.println("결과 값 : " + service.memberlogin(vo));
		
	}
}
