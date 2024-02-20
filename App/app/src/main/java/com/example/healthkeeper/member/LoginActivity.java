package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
            guardianlogin(binding.userId.getText().toString(),binding.userPw.getText().toString());
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

    public void guardianlogin(String guardian_id,String guardian_pw){
        CommonConn conn = new CommonConn("andlogin",this);
        conn.addParamMap("guardian_id" , guardian_id);
        conn.addParamMap("guardian_pw",guardian_pw);

        conn.onExcute((isResult, data) -> {
            Log.d("로그인", "guardianlogin: "+data);
            GuardianMemberVO vo = new Gson().fromJson(data, GuardianMemberVO.class);

            if(vo ==null){
                Toast.makeText(this,"아이디 또는 패스워드 틀림",Toast.LENGTH_SHORT).show();
                return;
            }else{
                MainActivity ma = (MainActivity)MainActivity._mainActivity;
                ma.finish();
                Log.d("로그인", "");
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }

}