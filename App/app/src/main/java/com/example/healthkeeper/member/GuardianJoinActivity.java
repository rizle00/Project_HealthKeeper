package com.example.healthkeeper.member;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityPatientJoinBinding;

public class GuardianJoinActivity extends AppCompatActivity {
    MemberVO vo = new MemberVO();
    ActivityPatientJoinBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientJoinBinding.inflate(getLayoutInflater());


        /* 혈액형 */
        Spinner bloodTypeSpn = (Spinner)binding.spnBloodType;
        ArrayAdapter bloodAdapter = ArrayAdapter.createFromResource(this,R.array.bloodType, android.R.layout.simple_spinner_item);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bloodTypeSpn.setAdapter(bloodAdapter);


        /* 아이디 글자수 확인 */
       IdLength();

        binding.btnJoin.setOnClickListener(v -> {
            joinClick();
        });


        setContentView(binding.getRoot());
    }



    public boolean idCheck(){
        binding.btnIdCheck.setOnClickListener(v -> {

        });
        /* 아이디 중복체크 완료되면 체크표시 */
        binding.edtUserId.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.img_check,0);
        return false;
    }

    public void joinClick(){

    }

    public void IdLength(){
        binding.edtUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int idLength = binding.edtUserId.getText().length();
                if(idLength <8 || idLength>16){
                    binding.tvWarningId.setVisibility(View.VISIBLE);
                    binding.btnIdCheck.setVisibility(View.GONE);
                }else{
                    binding.tvWarningId.setVisibility(View.GONE);
                    binding.btnIdCheck.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void pwCheck() {

    }
}