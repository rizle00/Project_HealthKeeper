package com.example.healthkeeper.main;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityCommunityBinding;
import com.example.healthkeeper.setting.SettingFragment;

public class CommunityActivity extends AppCompatActivity {
    ActivityCommunityBinding binding;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle("커뮤니티");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//부모 액티비티로의 이동이나 앱의 계층 구조 내에서 위로 이동하는 데 사용

        binding.bottomNav.setOnItemSelectedListener(item -> {//btm_menu 클릭시 설정
            int itemId = item.getItemId();

            if (itemId == R.id.btm_home) {
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.btm_emergency) {
                showEmergencyDialog();
                return true;
            } else if (itemId == R.id.btm_setting) {
                changeFragment(new SettingFragment());
                getSupportActionBar().setTitle("");
                return true;
            }
            return false;
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//툴바의 메뉴생성
        getMenuInflater().inflate(R.menu.toolbar_community, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){//툴바 메뉴 눌렀을때 전환
        int id= item.getItemId();
        if(id==R.id.menu_camera){
            Intent intent=new Intent(this, CCTVActivity.class);
            startActivity(intent);
            finish();
            return true;
        }else
        if(id==R.id.menu_condition){
            Intent intent= new Intent(this,ConditionActivity.class);
            startActivity(intent);
            finish();


            return true;
        }
        if(id==R.id.menu_logout){
            Intent intent=new Intent(this, LoginBeforeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }else if (id == android.R.id.home) { //toolbar의 back키 눌렀을 때 동작
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return  super.onOptionsItemSelected(item);
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


}


