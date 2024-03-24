package com.example.healthkeeper.member;

import android.app.ProgressDialog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityFindIdBinding;
import com.google.gson.Gson;

public class FindIdActivity extends AppCompatActivity {
    ActivityFindIdBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindIdBinding.inflate(getLayoutInflater());
        binding.btnFindId.setOnClickListener(v -> {
            usableInfo();
        });



        setContentView(binding.getRoot());

    }

    public void findId(){
        CommonConn conn = new CommonConn("andfindid",this);

        MemberVO vo = new MemberVO();
        vo.setNAME(binding.edtUserName.getText().toString());
        vo.setPHONE(binding.edtUserPhone.getText().toString());
        String voJson = new Gson().toJson(vo);
        conn.addParamMap("vo",voJson);
        conn.onExcute((isResult, data) -> {

                if(!data.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("회원님의 아이디는 '"+data + "' 입니다").setPositiveButton("확인",(dialog, which) -> {
                                finish();
                            })
                            .show();
                }else{
                    Toast.makeText(this, "해당 정보의 아이디는 없습니다",Toast.LENGTH_SHORT).show();
                }


        });
    }

    public void usableInfo(){
        if(binding.edtUserName.getText().toString().length() == 0 ||
                binding.edtUserPhone.getText().toString().length()==0){
            Toast.makeText(this, "정보를 모두 입력해주세요",Toast.LENGTH_SHORT).show();
        }else{
            findId();
        }
    }
}