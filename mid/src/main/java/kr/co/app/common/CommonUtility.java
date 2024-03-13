package kr.co.app.common;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:info.properties")
public class CommonUtility {
	

	@Value("${EMAIL}")	private String EMAIL;
	@Value("${EMAIL_PW}") private String EMAIL_PW;
	private void connectMailServer(HtmlEmail mail) {
		mail.setDebug(true);
		mail.setCharset("utf-8");
		
		mail.setHostName( "smtp.naver.com" );
		mail.setAuthentication(EMAIL, EMAIL_PW);
		System.out.println(EMAIL_PW);
		mail.setSSLOnConnect(true);
	}
	
	
	public boolean sendPassword(String email, String pw) {
		boolean send = true;
		
		HtmlEmail mail = new HtmlEmail();
		connectMailServer(mail);
		
		try {
			mail.setFrom(EMAIL,"healthKeeper");
			mail.addTo(email ,"이름이빠져서?");
			
			//메일 제목
			mail.setSubject( "스마트 IoT 로그인 임시비밀번호 발급" );
			//메일 내용
			StringBuffer content = new StringBuffer();
			content.append("<h3>") .append("임시 비밀번호가 발급되었습니다</h3>");
			content.append("<div>임시 비밀번호: <strong>") .append( pw ) .append( "</strong> </div>");
			content.append("<div>발급된 임시 비밀번호로 로그인 후 비밀번호를 변경하세요</div>");
			mail.setHtmlMsg( content.toString() );
			
			mail.send(); //메일 보내기 버튼 클릭
		} catch (EmailException e) {
			e.printStackTrace();
			send = false;
		}
		
		return send;
	}
}
