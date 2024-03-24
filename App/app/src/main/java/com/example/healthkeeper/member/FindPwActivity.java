package com.example.healthkeeper.member;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
                binding.edtMail.getText().toString().length()==0
            ){
            Toast.makeText(this, "정보를 모두 입력해주세요",Toast.LENGTH_SHORT).show();
        }else{
            findPw();
        }
    }

    public void findPw(){
        CommonConn conn = new CommonConn("checkinfo",this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        MemberVO vo = new MemberVO();
        vo.setNAME(binding.edtUserName.getText().toString());
        vo.setEMAIL(binding.edtUserEmail.getText().toString());
        vo.setPHONE(binding.edtUserPhone.getText().toString());
        String voJson = new Gson().toJson(vo);
        conn.addParamMap("vo",voJson);
        conn.addParamMap("mail",binding.edtMail.getText().toString());
        conn.onExcute((isResult, data) -> {
            if(data.equals("success")){
                builder.setMessage("완료되었습니다 메일을 확인해주세요").setPositiveButton("확인",(dialog, which) -> {
                    finish();
                });
                builder.show();
            }else if(data.equals("none")){
                Toast.makeText(this, "정보가 일치하지않습니다",Toast.LENGTH_SHORT).show();
            }else if(data.equals("failure")){
                builder.setMessage("오류가 발생했습니다 다시 시도해주세요");
                builder.show();
            }
        });

    }
}