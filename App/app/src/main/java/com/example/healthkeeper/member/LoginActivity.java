package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.healthkeeper.databinding.ActivityLoginBinding;
import com.example.healthkeeper.main.MainActivity;

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
            if(1==1){
                /*로그인 성공 */
                MainActivity ma = (MainActivity)MainActivity._mainActivity;
                ma.finish();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "아이디 혹은 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            }
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
}