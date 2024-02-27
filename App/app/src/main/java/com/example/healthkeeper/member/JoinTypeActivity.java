package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityJoinTypeBinding;

public class JoinTypeActivity extends AppCompatActivity {
    public static Activity joinTypeActivity;
    ActivityJoinTypeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        joinTypeActivity = JoinTypeActivity.this;
        binding = ActivityJoinTypeBinding.inflate(getLayoutInflater());

        binding.llPatient.setOnClickListener(v -> {
            Intent intent = new Intent(this,PatientJoinActivity.class);
            startActivity(intent);
        });

        binding.llGuardian.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuardianJoinActivity.class);
            startActivity(intent);
        });

        setContentView(binding.getRoot());

    }
}