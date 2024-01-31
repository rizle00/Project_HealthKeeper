package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import com.example.healthkeeper.databinding.ActivityLoginBinding;

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
            Intent  intent = new Intent(this, com.example.healthkeeper.member.MemberJoinActivity.class);
            startActivity(intent);
        });


        setContentView(binding.getRoot());
    }
}