package kr.co.and;

import com.google.gson.Gson;
import kr.co.and.firebase.FirebaseCloudMessageService;
import kr.co.and.firebase.RequestDTO;
import kr.co.and.firebase.TypeVO;
import kr.co.model.MemberVO;
import kr.co.service.AndMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FirebaseController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;
    @Autowired
    private AndMemberService service;

    @PostMapping("/api/fcm")
    public ResponseEntity pushEmergency(String params) throws IOException {
        System.out.println(params);
     RequestDTO dto = new Gson().fromJson(params, RequestDTO.class);
        TypeVO type = service.type(dto.getCATEGORY_ID());
        MemberVO info = service.guardian(dto.getGuardian_id());
        System.out.println(info.getNAME());
        System.out.println(service.insertAlarm(dto));
        firebaseCloudMessageService.sendMessageTo(
                info.getToken(),
                type.getTITLE(),
                dto.getName()+"님의"+type.getCONTENT());
        return ResponseEntity.ok().build();
    }
}
