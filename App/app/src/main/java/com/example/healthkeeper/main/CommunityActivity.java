package com.example.healthkeeper.main;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityCommunityBinding;
import com.example.healthkeeper.databinding.ActivityMainBinding;

public class CommunityActivity extends AppCompatActivity {
ActivityCommunityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}