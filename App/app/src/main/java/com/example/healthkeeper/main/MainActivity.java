package com.example.healthkeeper.main;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.setting.MyMenuFragment;

public class MainActivity extends AppCompatActivity {
    private AlertDialog alertDialog;
    ActivityMainBinding binding;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 툴바 추가
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기 버튼만들기

        changeFragment(new HomeFragment());


        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int customTextColor = getResources().getColor(R.color.white);

            if (itemId == R.id.nav_home) {
                changeFragment(new HomeFragment());
                getSupportActionBar().setTitle(getColoredSpanned("홈", customTextColor));//툴바에 표시될 타이틀과 색상지정!
                setToolbarMenu(R.menu.toolbar_home);

                return true;
            } else if (itemId == R.id.nav_119) {
                getSupportActionBar().setTitle(getColoredSpanned("긴급연락", customTextColor));
                showEmergencyDialog();
                return true;
            } else if (itemId == R.id.nav_commu) {
                //changeScheduleFragment(new ());
                getSupportActionBar().setTitle(getColoredSpanned("CUMUNITY", customTextColor));
                setToolbarMenu(R.menu.toolbar_cummunity);
                return true;
            } else if (itemId == R.id.nav_schedule) {
                changeScheduleFragment(new ScheduleFragment());
                getSupportActionBar().setTitle(getColoredSpanned("일정관리", customTextColor));
                setToolbarMenu(R.menu.toolbar_schedule);
                return true;
            } else if (itemId == R.id.nav_setting) {
                changeFragment(new MyMenuFragment());
                getSupportActionBar().setTitle(getColoredSpanned("나의 메뉴", customTextColor));
                setToolbarMenu(R.menu.toolbar_setting);
                return true;
            }

            return false;
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//툴바의 메뉴생성
        getMenuInflater().inflate(R.menu.toolbar_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//툴바선택시....
        int id = item.getItemId();

        if (id == R.id.menu_menu) {

            return true;
        } else if (id == R.id.menu_logout) {
            Intent intent = new Intent(this, LoginBeforeActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.menu_setting) {

            return true;
        }else if (id == android.R.id.home) { //toolbar의 back키 눌렀을 때 동작
            finish();
            return true;
        }
        // 다른 메뉴 아이템에 대한 처리 추가 가능

        return super.onOptionsItemSelected(item);
    }

    private SpannableString getColoredSpanned(String text, int color) {//툴바의 텍스트색상설정
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showEmergencyDialog() {//긴급전화 다이얼로그
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_emergency, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        dialogTitle.setText("긴급 전화");
        dialogMessage.setText("긴급 전화 연결됩니다.\n 연결하시겠습니까?");

        confirmButton.setOnClickListener((new View.OnClickListener() {//긴급전화 확인버튼
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:긴급전화번호"));
                startActivity(callIntent);

                alertDialog.dismiss();
            }
        }));

        cancelButton.setOnClickListener(new View.OnClickListener() {//긴급전화 취소버튼
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }

    public void changeScheduleFragment(ScheduleFragment fragment) {//일정관리메뉴 화면
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();//
    }

    private void setToolbarMenu(int menuResId) {//툴바메뉴 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        getMenuInflater().inflate(menuResId, toolbar.getMenu());
    }

    private void updateNotificationCount(int count) {//새로운 알림이 뜨면 업데이트된 알림갯수가 표시됨
        MenuItem menuItem = binding.toolbar.getMenu().findItem(R.id.menu_alam_notification_count);

        if (menuItem != null) {
            menuItem.setTitle(String.valueOf(count));
            menuItem.setVisible(count > 0);
        }
    }




    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();// Fragment를 뒤로가기

        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();

            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "한번 더 뒤로가기를 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // 2초 동안 한번 더 눌러야 종료
        }


    }
}