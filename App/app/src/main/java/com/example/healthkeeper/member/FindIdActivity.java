package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityFindIdBinding;
import com.google.gson.Gson;

public class FindIdActivity extends AppCompatActivity {
  /*  ActivityFindIdBinding binding;

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

        GuardianMemberVO vo = new GuardianMemberVO();
        vo.setGuardian_name(binding.edtUserName.getText().toString());
        vo.setGuardian_email(binding.edtUserEmail.getText().toString());
        vo.setGuardian_phone(binding.edtUserPhone.getText().toString());
        String voJson = new Gson().toJson(vo);
        conn.addParamMap("vo",voJson);
        conn.onExcute((isResult, data) -> {
            if(!data.equals("")){
                Intent intent = new Intent(this,FindResultActivity.class);
                intent.putExtra("id_result",data);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "해당 정보의 아이디는 없습니다",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void usableInfo(){
        if(binding.edtUserName.getText().toString().length() == 0 ||
                binding.edtUserEmail.getText().toString().length() == 0 ||
                binding.edtUserPhone.getText().toString().length()==0){
            Toast.makeText(this, "정보를 모두 입력해주세요",Toast.LENGTH_SHORT).show();
        }else{
            findId();
        }
    }*/
}