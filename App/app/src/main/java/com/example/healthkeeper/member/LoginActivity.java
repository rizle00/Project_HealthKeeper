package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityLoginBinding;
import com.example.healthkeeper.main.MainActivity;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    /* 화면 전환 -> intent
    아이디 비밀번호 -> finish() xx
    회원가입 -> finish()*/

    /*
    https://developer.android.com/training/transitions/start-activity?hl=ko
    intent animation
    */

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        binding.tvJoin.setOnClickListener(v -> {
            Intent  intent = new Intent(this, JoinTypeActivity.class);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(v -> {
            login(binding.userId.toString(),binding.userPw.toString());
        });

        binding.tvFindId.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindIdActivity.class);
            startActivity(intent);
        });

        binding.tvFindPw.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindPwActivity.class);
            startActivity(intent);
        });
        setContentView(binding.getRoot());
    }

    public void login(String user_id,String user_pw){
        CommonConn conn = new CommonConn("http://192.168.0.49/web/and",this);
        conn.addParamMap("user_id" , user_id);
        conn.addParamMap("user_pw",user_pw);

        conn.onExcute((isResult, data) -> {
            MemberVO vo = new Gson().fromJson(data,MemberVO.class);
            if(vo ==null){
                Toast.makeText(this,"아이디 또는 패스워드 틀림",Toast.LENGTH_SHORT).show();
                MainActivity ma = (MainActivity)MainActivity._mainActivity;
                ma.finish();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }

}