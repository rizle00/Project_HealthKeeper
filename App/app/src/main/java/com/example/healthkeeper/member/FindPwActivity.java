package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityFindPwBinding;

public class FindPwActivity extends AppCompatActivity {
    ActivityFindPwBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindPwBinding.inflate(getLayoutInflater());


        binding.btnFindPw.setOnClickListener(v -> {
            findPw();
        });

        setContentView(binding.getRoot());
    }

    public void findPw(){

    }
}