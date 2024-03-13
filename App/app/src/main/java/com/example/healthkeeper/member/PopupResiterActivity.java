package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityPopupResiterBinding;

public class PopupResiterActivity extends AppCompatActivity {
    ActivityPopupResiterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPopupResiterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}