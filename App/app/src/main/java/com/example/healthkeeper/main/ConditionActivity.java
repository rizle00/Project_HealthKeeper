package com.example.healthkeeper.main;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityConditionBinding;
import com.example.healthkeeper.databinding.ActivityMainBinding;

public class ConditionActivity extends AppCompatActivity {
  ActivityConditionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}