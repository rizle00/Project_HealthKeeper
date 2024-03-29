package kr.co.app.firebase;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FirebaseMessageService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/health-f5329/messages:send";  // �슂泥��쓣 蹂대궪 �뿏�뱶�룷�씤�듃
    private ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
//        String token = dataHolder.getData();
        String message = makeMessage(targetToken, title, body);
//        dLFqJnOtSkiO8fx-bngRk9:APA91bG3-Xb-DF3BNIQtR65Uy4x2smS5FfZJ_TIrJ19svYLRVdhwv3m-mjtY652Sg9o0gpsmECflC_G13IkyJsLn7Wsn9o8xpzOstwmVUwI4lyIB5cHusTbQpMikTXZEbsTflysYfJ3w
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();


        System.out.println("11111"+response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();
        objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "serviceAccountKey.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));           //�뿊�꽭�뒪 �넗�겙 諛쏆븘�삤湲�

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

}
