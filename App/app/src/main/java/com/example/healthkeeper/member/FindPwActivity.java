package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityFindPwBinding;
import com.google.gson.Gson;

public class FindPwActivity extends AppCompatActivity {
    ActivityFindPwBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindPwBinding.inflate(getLayoutInflater());


        binding.btnFindPw.setOnClickListener(v -> {
            infoPw();
        });

        setContentView(binding.getRoot());
    }

    public void infoPw(){
        if(binding.edtUserName.getText().toString().length() == 0 ||
                binding.edtUserEmail.getText().toString().length() == 0 ||
                binding.edtUserPhone.getText().toString().length()==0 ||
                binding.edtUserId.getText().toString().length()==0
            ){
            Toast.makeText(this, "정보를 모두 입력해주세요",Toast.LENGTH_SHORT).show();
        }else{
            findPw();
        }
    }

    public void findPw(){
        CommonConn conn = new CommonConn("andfindpw",this);

        GuardianMemberVO vo = new GuardianMemberVO();
        vo.setGuardian_name(binding.edtUserName.getText().toString());
        vo.setGuardian_email(binding.edtUserEmail.getText().toString());
        vo.setGuardian_phone(binding.edtUserPhone.getText().toString());
        vo.setGuardian_email(binding.edtUserEmail.getText().toString());
        String voJson = new Gson().toJson(vo);
        conn.addParamMap("vo",voJson);
        conn.onExcute((isResult, data) -> {
            if(!data.equals("")){
                Intent intent = new Intent(this,FindResultActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "정보가 일치하지않습니다",Toast.LENGTH_SHORT).show();
            }
        });

    }
}