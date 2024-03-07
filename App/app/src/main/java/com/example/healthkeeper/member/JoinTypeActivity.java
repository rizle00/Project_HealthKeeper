package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.healthkeeper.databinding.ActivityJoinTypeBinding;

public class JoinTypeActivity extends AppCompatActivity {
    public static Activity joinTypeActivity;
    ActivityJoinTypeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        joinTypeActivity = JoinTypeActivity.this;
        binding = ActivityJoinTypeBinding.inflate(getLayoutInflater());
        Intent intent = new Intent(this,JoinActivity.class);
        binding.llPatient.setOnClickListener(v -> {
            intent.putExtra("type","patient");
            startActivity(intent);
        });

        binding.llGuardian.setOnClickListener(v -> {
            intent.putExtra("type","guardian");
            startActivity(intent);
        });

        setContentView(binding.getRoot());

    }
}