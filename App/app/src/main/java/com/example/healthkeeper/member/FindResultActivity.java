package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityFindResultBinding;

public class FindResultActivity extends AppCompatActivity {
    ActivityFindResultBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Intent intent = getIntent();
//        binding.tvResultId.setText(intent.getStringExtra("id_result"));
        setContentView(binding.getRoot());

    }
}