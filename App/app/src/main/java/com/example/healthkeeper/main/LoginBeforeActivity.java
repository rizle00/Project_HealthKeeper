package com.example.healthkeeper.main;

import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.example.healthkeeper.databinding.ActivityLoginBeforeBinding;
import com.example.healthkeeper.member.LoginActivity;

public class LoginBeforeActivity extends AppCompatActivity {
    /* 로그인 되면 종료하게 하기위함*/
//    public static LoginBeforeActivity _loginBeforeActivity;
//    private SharedPreferences sharedPreferences;
//    private static final String PREFS_NAME = "MyPrefs";
//    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final int REQUEST_LOGIN = 10012;
 ActivityLoginBeforeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBeforeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        _loginBeforeActivity = this;

//        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        // CardView 내부의 ImageView 또는 TextView에 대한 onClick 리스너 설정

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginBeforeActivity.this,LoginActivity.class));
                Intent intent = new Intent(LoginBeforeActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                // 로그인 성공 처리
                // 이곳에서 로그인 후의 작업을 수행할 수 있습니다.
                startActivity(new Intent(LoginBeforeActivity.this,MainActivity.class));
            } else {
                // 로그인 실패 또는 취소 처리
                // 이곳에서 실패 또는 취소에 대한 작업을 수행할 수 있습니다.
                Toast.makeText(LoginBeforeActivity.this, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
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