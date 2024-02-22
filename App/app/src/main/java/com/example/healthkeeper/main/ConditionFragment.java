package com.example.healthkeeper.main;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.healthkeeper.R;



import com.example.healthkeeper.databinding.FragmentConditionBinding;
import com.google.android.material.chip.ChipGroup;

public class ConditionFragment extends Fragment {
    FragmentConditionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConditionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        int customTextColor= getResources().getColor(R.color.white);

        if (actionBar != null) {
            actionBar.setTitle(getColoredSpanned("현재상태", customTextColor));
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
                } else if (checkedId == R.id.data) {
                    // 통계 Chip이 선택된 경우
                    changeFragment(new DataFragment());
                }
            }
        });
    }


    private SpannableString getColoredSpanned(String text, int color) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    private void changeFragment(Fragment fragment) {/*일반적으로 getSupportFragmentManager()은 액티비티에서 프래그먼트를 관리하는 데 사용되고,
                                                      getChildFragmentManager()은 프래그먼트 내에서 자식 프래그먼트를 관리하는 데 사용*/
        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit(); }

}
