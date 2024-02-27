package com.example.healthkeeper.main;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityLoginBeforeBinding;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.member.LoginActivity;

public class LoginBeforeActivity extends AppCompatActivity {
    /* 로그인 되면 종료하게 하기위함*/
    public static LoginBeforeActivity _loginBeforeActivity;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
 ActivityLoginBeforeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBeforeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        _loginBeforeActivity = this;

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        // CardView 내부의 ImageView 또는 TextView에 대한 onClick 리스너 설정

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginBeforeActivity.this,LoginActivity.class));

            }
        });
        binding.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialogAndNavigate();
            }
        });



        binding.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialogAndNavigate();
            }
        });



        binding.img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialogAndNavigate();
            }
        });



        binding.img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialogAndNavigate();
            }
        });



    }

    private void showLoginDialogAndNavigate() {
        // 다이얼로그 생성!
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("로그인이 필요합니다. 로그인 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 로그인 화면으로 이동
                        Intent intent = new Intent(LoginBeforeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // 다이얼로그 보이기
        AlertDialog alert = builder.create();
        alert.show();
    }
}