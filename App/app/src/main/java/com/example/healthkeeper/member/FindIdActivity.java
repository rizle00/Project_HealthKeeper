package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityFindIdBinding;
import com.google.gson.Gson;

public class FindIdActivity extends AppCompatActivity {
    ActivityFindIdBinding binding;
    EditText mail = binding.edtUserEmail;
    EditText phone = binding.edtUserPhone;
    EditText name = binding.edtUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindIdBinding.inflate(getLayoutInflater());

        binding.btnFindId.setOnClickListener(v -> {

            findId();
        });



        setContentView(binding.getRoot());
    }

    public void findId(){
        CommonConn conn = new CommonConn("andfindid",this);

        GuardianMemberVO vo = new GuardianMemberVO();
        vo.setGuardian_name(name.getText().toString());
        vo.setGuardian_email(mail.getText().toString());
        vo.setGuardian_phone(phone.getText().toString());

        String voJson = new Gson().toJson(vo);
        conn.addParamMap("vo",voJson);
        conn.onExcute((isResult, data) -> {
            if(data!=null){

            }else{

            }
        });
    }

    public void usableInfo(){
        if(mail.getText().toString().length() == 0 ||
        phone.getText().toString().length() == 0 ||
        name.getText().toString().length()==0){
            Toast.makeText(this, "정보를 모두 입력해주세요",Toast.LENGTH_SHORT).show();
        }else{
            findId();
        }
    }
}