package kr.co.app.config;

import com.google.gson.Gson;
import kr.co.app.member.MemberService;
import kr.co.app.member.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@RestController
@RequiredArgsConstructor
public class FirebaseController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;
    @Autowired
    private MemberService service;

    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(String params) throws IOException {
        System.out.println(params);
     RequestDTO vo = new Gson().fromJson(params, RequestDTO.class);
//        System.out.println(vo.getTargetToken());
        MemberVO info = service.guardian(vo.getGuardian_id());
//        service.insertAlarm;

//        System.out.println(requestDTO.getTargetToken() + " "
//                +requestDTO.getTitle() + " " + requestDTO.getBody()+requestDTO.getGuardian_id());
//
        firebaseCloudMessageService.sendMessageTo(
                info.getToken(),
//                vo.getTargetToken(),
                vo.getTitle(),
                vo.getBody());
        return ResponseEntity.ok().build();
    }
}
