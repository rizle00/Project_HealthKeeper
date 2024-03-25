package com.example.healthkeeper.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.ActivityPushBinding;
import com.example.healthkeeper.setting.HospitalActivity;
import com.example.healthkeeper.setting.SwipeDismissRecyclerViewTouchListener;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.List;

public class PushActivity extends AppCompatActivity implements PushAdapter.OnDataChangedListener {

    ActivityPushBinding binding;
    CommonRepository repository;
    CommonConn conn;
    SharedPreferences pref;
    private RecyclerView rev;
    private PushAdapter adapter; // 어댑터 객체 선언
    private List<AlarmLogVO> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPushBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int customTextColor = getResources().getColor(R.color.white);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//툴바에 뒤로가기 아이콘 생성

        getSupportActionBar().setTitle(getColoredSpanned("알림", customTextColor));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        repository = new CommonRepository(((App) getApplication()).executorService);

        pref = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
        loadAlarmLog(pref.getString("user_id",""));

    }
    private SpannableString getColoredSpanned(String text, int color) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void loadAlarmLog(String id) {
        conn = new CommonConn("member/alarmLog");
        conn.addParamMap("params", id);
//        conn.addParamMap("params", pref.getString("user_id",""));
        repository.select(conn).thenAccept(result -> {
            list = new Gson().fromJson(result, new TypeToken<List<AlarmLogVO>>() {
            }.getType());
            if (!list.isEmpty()) {
                createRev();
            } else {
                getSupportActionBar().setTitle(getColoredSpanned("새로운 알림이 없습니다",  getResources().getColor(R.color.white)));
//                binding.tvNone.setVisibility(View.VISIBLE);
            }
        });


    }

    private void createRev() {
        rev = binding.recvPush;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rev.setLayoutManager(layoutManager);

        String id = pref.getString("user_id", "");
        adapter = new PushAdapter(getLayoutInflater(), list, PushActivity.this, repository, "2", this);
        rev.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // 현재 액티비티 종료
    }


    @Override
    public void onDataChanged() {
        if (adapter.mDataset.isEmpty()) getSupportActionBar().setTitle(getColoredSpanned("새로운 알림이 없습니다",  getResources().getColor(R.color.white)));
    }
}