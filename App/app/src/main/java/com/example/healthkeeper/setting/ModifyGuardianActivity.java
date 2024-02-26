package com.example.healthkeeper.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityModifyGuardianBinding;

public class ModifyGuardianActivity extends AppCompatActivity {
    ActivityModifyGuardianBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyGuardianBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getPreference();

    }

    /*로그인 정보 가져오기*/
    public void getPreference(){
       getSharedPreferences("PROJECT_MEMBER",MODE_PRIVATE).getString("user_id","");
    }
}