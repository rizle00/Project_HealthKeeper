package com.example.healthkeeper.main.monitor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.ActivityConditionBinding;

import com.google.android.material.chip.ChipGroup;

public class ConditionActivity extends AppCompatActivity {
     ActivityConditionBinding binding;



    private SharedPreferences pref;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConditionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        pref = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
        user_id = pref.getString("user_id","");
        ActionBar actionBar = getSupportActionBar();
        int customTextColor = getResources().getColor(R.color.white);

        if (actionBar != null) {
            actionBar.setTitle(getColoredSpanned("현재상태", customTextColor));
        }

        changeFragment(new TemperatureFragment());

        binding.chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            //chip선택할때 변경되는fragment설정하기
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId == R.id.chipTemperature) {
                    // 체온 Chip
                    changeFragment(new TemperatureFragment());
                } else if (checkedId == R.id.chipHeartRate) {
                    // 심박 Chip
                    changeFragment(new HeartRateFragment());
                } else if (checkedId == R.id.data) {
                    // 통계 Chip
                    changeFragment(new DataFragment());
                    binding.chipGroup.setBackgroundColor(getResources().getColor(R.color.white));


                }
            }
        });
    }



    private SpannableString getColoredSpanned(String text, int color) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.condition_container, fragment)
                .commit();
    }


    public String getUser_id() {
        return user_id;
    }
}
