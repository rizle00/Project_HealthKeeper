package kr.co.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service @PropertySource("classpath:info.properties")
public class CommonUtility {
    public String requestAPI( String apiURL ) {

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8") );
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
            }
            String inputLine;
            StringBuilder res = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            if (responseCode == 200) {
                apiURL = res.toString();
                System.out.println(res.toString() );
            }
        } catch (Exception e) {
            // Exception 로깅
        }
        return apiURL;
    }


    public String requestAPI( String apiURL, String property ) {
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", property);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8") );
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
            }
            String inputLine;
            StringBuilder res = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            if (responseCode == 200) {
                apiURL = res.toString();
                System.out.println(res.toString() );
            }
        } catch (Exception e) {
            // Exception 로깅
        }
        return apiURL;
    }
    private String redirectURL(HttpSession session, Model model) {
        if( session.getAttribute("redirect") == null ) {
            return "redirect:/";
        }else{
            HashMap<String, Object> map
                    = (HashMap<String, Object>)session.getAttribute("redirect");
            model.addAttribute("url", map.get("url"));
            model.addAttribute("id", map.get("id"));
            model.addAttribute("page", map.get("page"));

            session.removeAttribute("redirect");
            return "include/redirect";
        }
    }

    public String appURL(HttpServletRequest request) {
        StringBuffer url = new StringBuffer("http://");
        url.append( request.getServerName() ).append(":"); 	// http://localhost:, http://127.0.0.1:
        url.append( request.getServerPort() ); 				// http://localhost:80, http://127.0.0.1:8080
        url.append( request.getContextPath() ); 			// http://localhost:80/smart, http://127.0.0.1:8080/web
        return url.toString();
    }

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
