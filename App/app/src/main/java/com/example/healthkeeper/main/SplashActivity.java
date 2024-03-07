package com.example.healthkeeper.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.healthkeeper.databinding.ActivitySplashBinding;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "SplashActivity";
    private ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "onCreate");




        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 선택 사항: 새로 시작한 활동 이후 현재 활동을 종료합니다.
        }, 2000);

    }


    @Override
    public void onClick(View v) {

    }
}