package com.example.healthkeeper.member;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityMemberJoinBinding;

public class MemberJoinActivity extends AppCompatActivity {
    MemberVO vo;
    ActivityMemberJoinBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberJoinBinding.inflate(getLayoutInflater());


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
            bloodCheck();
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

    public void bloodCheck() {
        Spinner bloodType = (Spinner) binding.spnBloodType;
        String blood = bloodType.getSelectedItem().toString();
        if (blood.equals("혈액형")) {
            Log.d("실패", blood);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("").setMessage("혈액형을 확인해주세요");
            builder.show();

        }
        else{
            Log.d("성공", blood);
            vo.setBlood(blood);
        }
    }
}