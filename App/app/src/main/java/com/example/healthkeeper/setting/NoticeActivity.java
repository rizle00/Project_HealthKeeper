package com.example.healthkeeper.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;

import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityNoticeBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {
    ActivityNoticeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        binding = ActivityNoticeBinding.inflate(getLayoutInflater());

        NoticeAdapter nAdapter = new NoticeAdapter(getLayoutInflater(),getNotice());
        binding.recvNotice.setAdapter(nAdapter);
        binding.recvNotice.setLayoutManager(new LinearLayoutManager(this));
    }

    public ArrayList<String> getNotice(){
        CommonConn conn =  new CommonConn("andnotice",this);
        conn.onExcute((isResult, data) -> {
            List<NoticeVO> noticeList = new Gson().fromJson(data, new TypeToken<List<NoticeVO>>(){}.getType());
        });
        return null;
    }
}