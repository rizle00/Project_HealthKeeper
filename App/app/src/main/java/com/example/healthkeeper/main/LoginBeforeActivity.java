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
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityLoginBeforeBinding;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.member.LoginActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LoginBeforeActivity extends AppCompatActivity {
    ViewPager viewPager;
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


        // 이미지 리소스 ID 목록 생성
        ArrayList<Integer> imageResources = new ArrayList<>();
        imageResources.add(R.drawable.watch_img1);
        imageResources.add(R.drawable.img_cctv);
        imageResources.add(R.drawable.watch_img2);
        imageResources.add(R.drawable.watch_img6);
        imageResources.add(R.drawable.watch_img5);
        imageResources.add(R.drawable.watch_img1);

        // FragmentManager를 가져옴
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Autoimg_Adapter를 생성하고 ViewPager에 설정
        Autoimg_Adapter adapter = new Autoimg_Adapter(fragmentManager, imageResources);
        viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(adapter);

        // 타이머 설정하여 이미지 자동 전환
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < adapter.getCount() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 5000, 5000); // 5초마다 이미지 전환
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