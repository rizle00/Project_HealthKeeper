package com.example.healthkeeper.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.healthkeeper.R;

public class LoginActivity extends AppCompatActivity {

    /* 화면 전환 -> intent
    아이디 비밀번호 -> finish() xx
    회원가입 -> finish()*/

    /*
    https://developer.android.com/training/transitions/start-activity?hl=ko
    intent animation
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}