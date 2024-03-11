package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
            if(getIntent().getStringExtra("social")==null) {
                Intent intent = new Intent(this,JoinActivity.class);
                intent.putExtra("type", "patient");
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this,SimpleJoinActivity.class);
                intent.putExtra("social",getIntent().getStringExtra("social"));
                intent.putExtra("type", "patient");
                Log.d("intent", "onCreate: "+getIntent().getStringExtra("social"));
                startActivity(intent);
            }
        });

        binding.llGuardian.setOnClickListener(v -> {
            if(getIntent().getStringExtra("social")==null) {
                Intent intent = new Intent(this, JoinActivity.class);
                intent.putExtra("type", "guardian");
                startActivity(intent);
            }else{
                Intent intent = new Intent(this,SimpleJoinActivity.class);
                intent.putExtra("social",getIntent().getStringExtra("social"));
                intent.putExtra("type", "patient");
                Log.d("intent", "onCreate: "+getIntent().getStringExtra("social"));
                startActivity(intent);
            }
        });

        setContentView(binding.getRoot());

    }
}