package com.example.healthkeeper.main;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.healthkeeper.databinding.ActivitySplashBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "SplashActivity";
    private ActivitySplashBinding binding;

    private SharedPreferences preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token();
        Log.d(TAG, "onCreate");
//      String name =  preference.getString("user_name","Guest");

      String name = "a";

        new Handler().postDelayed(() -> {
            //로그인 체크
            if(name.equals("Guest")){
                Intent intent = new Intent(SplashActivity.this, LoginBeforeActivity.class);
                startActivity(intent);
                finish(); // 선택 사
            }else{
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
           // 선택 사항: 새로 시작한 활동 이후 현재 활동을 종료합니다.
        }, 2000);

    }

    private void token(){
        SharedPreferences preference = getSharedPreferences("PROJECT_MEMBER",MODE_PRIVATE);
        String savedToken = preference.getString("token", null);
        SharedPreferences.Editor editor = preference.edit();


        // 이미 저장된 토큰이 있다면 getToken() 호출을 생략합니다.
        // 새로운 토큰 추후에..
        if (savedToken != null) {
            Log.d(TAG, "Token already saved: " + savedToken);
            return; // 이미 저장된 토큰이 있으므로 더 이상 진행하지 않습니다.
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        editor.putString("token", token);
                        editor.apply();
                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);

                        Log.d(TAG, token);
                    }
                });

    }

    @Override
    public void onClick(View v) {

    }
}