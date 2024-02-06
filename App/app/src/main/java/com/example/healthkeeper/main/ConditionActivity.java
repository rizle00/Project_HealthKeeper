package com.example.healthkeeper.main;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityConditionBinding; // 바인딩 클래스 import 추가
import com.google.android.material.chip.ChipGroup;

public class ConditionActivity extends AppCompatActivity {
    ActivityConditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("현재 상태"); // 타이틀 설정
        }

        changeFragment(new TemperatureFragment());

        binding.chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId == R.id.chipTemperature) {
                    // 체온 Chip이 선택된 경우
                    changeFragment(new TemperatureFragment());
                } else if (checkedId == R.id.chipHeartRate) {
                    // 심박 Chip이 선택된 경우
                    changeFragment(new HeartRateFragment());
                }
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
