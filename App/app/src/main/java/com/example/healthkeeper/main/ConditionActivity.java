package com.example.healthkeeper.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityConditionBinding; // 바인딩 클래스 import 추가

public class ConditionActivity extends AppCompatActivity {
    ActivityConditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        changeFragment(new TemperatureFragment());

        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipTemperature) {
                // 체온
                changeFragment(new TemperatureFragment());
            } else if (checkedId == R.id.chipHeartRate) {
                // 심박
                changeFragment(new HeartRateFragment());
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
