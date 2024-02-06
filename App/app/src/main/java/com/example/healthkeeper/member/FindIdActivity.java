package com.example.healthkeeper.member;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityFindIdBinding;

public class FindIdActivity extends AppCompatActivity {
    ActivityFindIdBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindIdBinding.inflate(getLayoutInflater());

        binding.btnFindId.setOnClickListener(v -> {
            findId();
        });



        setContentView(binding.getRoot());
    }

    public void findId(){

    }
}