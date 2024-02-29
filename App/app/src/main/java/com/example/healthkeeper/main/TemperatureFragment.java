package com.example.healthkeeper.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.healthkeeper.R;

import com.example.healthkeeper.databinding.FragmentTemperatureBinding;

public class TemperatureFragment extends Fragment {


    FragmentTemperatureBinding binding;
    double trueTemperature=0;

    private String PREFS_NAME="MyTempPrefs";
    private String  KEY_SELECTED_COLOR="selectedColor";
    private String KEY_SELECTED_TEXT_COLOR="selectedTextColor";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTemperatureBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadColorSettings();

        binding.colorChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorChangeDialog();
            }
        });

        return view;
    }


    private void currentState(double trueTemperature) {// 체온결과에 따라 tv_sate 텍스트 업데이트해주도록 설정

        if (trueTemperature >= 35.9 && trueTemperature < 37.6) {
            binding.tvSate.setText("양  호");
        } else if (trueTemperature <= 35.8) {
            binding.tvSate.setText("저체온");
        } else {
            binding.tvSate.setText("고열");
        }
    }




    private void showColorChangeDialog() {//배경생상바꿀수있게!
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_color_select, null);
        RadioGroup colorChangeGroup = dialogView.findViewById(R.id.colorChangeGroup);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setView(dialogView);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedColor = 0;
                int selectedTextColor = Color.WHITE; // 기본적으로 텍스트 색상을 화이트로 설정

                int checkedRadioButtonId = colorChangeGroup.getCheckedRadioButtonId();

                if (checkedRadioButtonId != -1) { // 어떤 라디오 버튼이 선택되었는지 확인
                    RadioButton checkedRadioButton = dialogView.findViewById(checkedRadioButtonId);

                    if (checkedRadioButton.getId() == R.id.radioColor1) {
                        selectedColor = ContextCompat.getColor(requireContext(), R.color.radioColor1);
                        selectedTextColor = Color.BLACK;
                    } else if (checkedRadioButton.getId() == R.id.radioColor2) {
                        selectedColor = ContextCompat.getColor(requireContext(), R.color.radioColor2);
                    } else if (checkedRadioButton.getId() == R.id.radioColor3) {
                        selectedColor = ContextCompat.getColor(requireContext(), R.color.radioColor3);
                    } else if (checkedRadioButton.getId() == R.id.radioColor4) {
                        selectedColor = ContextCompat.getColor(requireContext(), R.color.radioColor4);
                    } else if (checkedRadioButton.getId() == R.id.radioColor5) {
                        selectedColor = ContextCompat.getColor(requireContext(), R.color.radioColor5);
                    }
                }

                saveColorSettings(selectedColor,selectedTextColor);
                binding.temperatureLinearlayout.setBackgroundColor(selectedColor);
                binding.tvSate.setTextColor(selectedTextColor);
            }
        });

        builder.setNegativeButton("취소", null);
        builder.show();
    }

    private void saveColorSettings(int selectedColor, int selectedTextColor) {//바꾼배경 설정저장
        SharedPreferences preferences=requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(KEY_SELECTED_COLOR,selectedColor);
        editor.putInt(KEY_SELECTED_TEXT_COLOR,selectedTextColor);
        editor.apply();

    }
    private void loadColorSettings(){
        SharedPreferences preferences=requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int selectedColor= preferences.getInt(KEY_SELECTED_COLOR,0);
        int selectedTextColor= preferences.getInt(KEY_SELECTED_TEXT_COLOR,Color.WHITE);

        binding.temperatureLinearlayout.setBackgroundColor(selectedColor);
        binding.tvSate.setTextColor(selectedTextColor);
    }
}

