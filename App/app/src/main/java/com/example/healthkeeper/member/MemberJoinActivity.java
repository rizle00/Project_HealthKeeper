package com.example.healthkeeper.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityMemberJoinBinding;

public class MemberJoinActivity extends AppCompatActivity {
    ActivityMemberJoinBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberJoinBinding.inflate(getLayoutInflater());


        /* 아이디 글자수 확인 */
        binding.userId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int idLength = binding.userId.getText().length();
                if(idLength <8 || idLength>16){
                    binding.tvWarningId.setVisibility(View.VISIBLE);
                }else{
                    binding.tvWarningId.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        setContentView(binding.getRoot());
    }
}