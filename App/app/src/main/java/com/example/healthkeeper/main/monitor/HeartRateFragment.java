package com.example.healthkeeper.main.monitor;

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

import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.bluetooth.BluetoothViewModel;
import com.example.healthkeeper.databinding.FragmentHeartRateBinding;

public class HeartRateFragment extends Fragment {

    private FragmentHeartRateBinding binding;
    //    double trueHeartrate=0;
    private String PREFS_NAME = "MyHeartPrefs";
    private String KEY_SELECTED_COLOR = "selectedColor";
    private String KEY_SELECTED_TEXT_COLOR = "selectedTextColor";
    private String KEY_SELECTED_TEXT_COLOR2 = "selectedTextColor2";
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHeartRateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        textView = binding.trueHeartrate;
        BluetoothViewModel sharedViewModel = ((App) requireActivity().getApplicationContext()).getSharedViewModel();
        sharedViewModel.getHeartLiveData().observe(getViewLifecycleOwner(), data -> {

                currentState(data);
                textView.setText(String.valueOf(data));


        });
        loadColorSettings();

        binding.colorChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorChangeDialog();
            }
        });

        return view;
    }


    private void currentState(int trueHeartrate) {// 심박수결과에 따라 tv_sate 텍스트 업데이트해주도록 설정
        if (trueHeartrate == 0) {
            binding.tvSate.setText("심박 정보 없음");
        } else if (trueHeartrate >= 60 && trueHeartrate < 81) {
            binding.tvSate.setText("양  호");
        } else if (trueHeartrate <= 59) {
            binding.tvSate.setText("낮  음");//or 저심박수
        } else {
            binding.tvSate.setText("높  음");//or  빈맥???
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
                int selectedTextColor2 = Color.BLACK; // 기본적으로 텍스트 색상을 블랙으로 설정

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
                        selectedTextColor2 = Color.WHITE;
                    } else if (checkedRadioButton.getId() == R.id.radioColor4) {
                        selectedColor = ContextCompat.getColor(requireContext(), R.color.radioColor4);
                    } else if (checkedRadioButton.getId() == R.id.radioColor5) {
                        selectedColor = ContextCompat.getColor(requireContext(), R.color.radioColor5);
                    }
                }

                saveColorSettings(selectedColor, selectedTextColor, selectedTextColor2);

                setChipGroupBackgroundColor(selectedColor);


                binding.HeartlateLinearlayout.setBackgroundColor(selectedColor);
                binding.tvSate.setTextColor(selectedTextColor);
                binding.tvTitle.setTextColor(selectedTextColor2);
            }
        });

        builder.setNegativeButton("취소", null);
        builder.show();
    }

    private void saveColorSettings(int selectedColor, int selectedTextColor, int selectedTextColor2) {//바꾼배경 설정저장
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_SELECTED_COLOR, selectedColor);
        editor.putInt(KEY_SELECTED_TEXT_COLOR, selectedTextColor);
        editor.putInt(KEY_SELECTED_TEXT_COLOR2, selectedTextColor2);
        editor.apply();

    }

    private void loadColorSettings() {//저장된 배경설정 불러오기
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int selectedColor = preferences.getInt(KEY_SELECTED_COLOR, 0);
        int selectedTextColor = preferences.getInt(KEY_SELECTED_TEXT_COLOR, Color.WHITE);
        int selectedTextColor2 = preferences.getInt(KEY_SELECTED_TEXT_COLOR2, Color.BLACK);

        binding.HeartlateLinearlayout.setBackgroundColor(selectedColor);
        binding.tvSate.setTextColor(selectedTextColor);
        binding.tvTitle.setTextColor(selectedTextColor2);
        setChipGroupBackgroundColor(selectedColor);
    }

    private void setChipGroupBackgroundColor(int selectedColor) {
        if (getActivity() instanceof ConditionActivity) {
            ConditionActivity conditionActivity = (ConditionActivity) getActivity();
            if (conditionActivity.binding != null && conditionActivity.binding.chipGroup != null) {
                conditionActivity.binding.chipGroup.setBackgroundColor(selectedColor);
            }
        }
    }
}
