package com.example.healthkeeper.member;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonClient;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonService;
import com.example.healthkeeper.databinding.ActivitySimpleJoinBinding;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleJoinActivity extends AppCompatActivity {
 /*   ActivitySimpleJoinBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySimpleJoinBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        CommonService apiInterface = CommonClient.getRetrofit().create(CommonService.class);
        HashMap<String,Object> params = new HashMap<>();

        binding.btnIdCheck.setOnClickListener(v -> {

        });

        *//* 아이디 유효성 메소드 *//*
        usableIdCheck();

        *//*아이디중복확인*//*
        idDupCheck();


        binding.btnJoin.setOnClickListener(v -> {
            joinClick();
        });

        *//* 혈액형 *//*
        Spinner bloodTypeSpn = (Spinner)binding.spnBloodType;
        ArrayAdapter bloodAdapter = ArrayAdapter.createFromResource(this,R.array.bloodType, android.R.layout.simple_spinner_item);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bloodTypeSpn.setAdapter(bloodAdapter);

        *//*혈액형 선택되었는지 메소드*//*
        bloodCheck();

    }
    public void idDupCheck(){
        binding.btnIdCheck.setOnClickListener(v -> {
            idDupCheck(binding.edtUserId.getText().toString());
        });
        *//* 아이디 중복체크 완료되면 체크표시 *//*

    }

    public void joinClick(){
        usableIdCheck();
        mailPatterns();
        phonePattern();




        int num = binding.tvWarningId.getVisibility()
                + binding.tvWarningEmail.getVisibility()+binding.tvWarningPhone.getVisibility()+binding.tvWarningGender.getVisibility();
        if(num ==32){

            Intent intent = getIntent();
            JoinTypeActivity jta = (JoinTypeActivity)JoinTypeActivity.joinTypeActivity;
            CommonConn conn = new CommonConn("andjoin",this);
            GuardianMemberVO vo = new GuardianMemberVO();
            vo.setGuardian_id(binding.edtUserId.getText().toString());
            vo.setGuardian_email(binding.edtAddress.getText().toString());
            vo.setSocial(intent.getStringExtra("social"));
            vo.setGuardian_phone(binding.edtUserPhone.getText().toString());
            vo.setPatient_id(binding.edtGuardianId.getText().toString());
            vo.setGuardian_name(binding.edtUserName.getText().toString());
            String voJson = new Gson().toJson(vo);
            conn.addParamMap("vo",voJson);


            conn.onExcute((isResult, data) -> {

            });


            jta.finish();
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정보가 올바르지 않습니다");
            builder.show();
        }
    }

    *//*아이디 길이 확인*//*
    public void usableIdCheck(){
        binding.tvWarningId.setText("아이디를 7~20자로 입력해주세요");
        binding.edtUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int idLength = binding.edtUserId.getText().length();
                if(idLength <7 || idLength>16){
                    binding.tvWarningId.setText("아이디를 7~20자로 입력해주세요");
                    binding.tvWarningId.setVisibility(View.VISIBLE);
                    binding.btnIdCheck.setVisibility(View.GONE);
                }else if(!isIdPattern()){
                    binding.tvWarningId.setText("영어 소문자와 숫자만 가능합니다");
                    binding.tvWarningId.setVisibility(View.VISIBLE);
                    binding.btnIdCheck.setVisibility(View.GONE);
                }else if (binding.edtUserId.getText().toString()==""){
                    binding.tvWarningId.setText("아이디를 입력해주세요");
                    binding.tvWarningId.setVisibility(View.VISIBLE);
                    binding.btnIdCheck.setVisibility(View.GONE);
                }
                else
                {
                    binding.tvWarningId.setVisibility(View.GONE);
                    binding.btnIdCheck.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    *//*비밀번호 영문, 숫자, 특문 정규식*//*


    public void mailPatterns(){
        Pattern mail_pattern = Patterns.EMAIL_ADDRESS;
        if(mail_pattern.matcher(binding.edtUserEmail.getText().toString()).matches()){
            binding.tvWarningEmail.setVisibility(View.GONE);
        }else {
            binding.tvWarningEmail.setText("이메일을 확인해주세요");
            binding.tvWarningEmail.setVisibility(View.VISIBLE);
        }
    }
    public void phonePattern(){
        Pattern phone_pattern = Patterns.PHONE;
        if(phone_pattern.matcher(binding.edtUserPhone.getText().toString()).matches()){
            binding.tvWarningPhone.setVisibility(View.GONE);
        }else{
            binding.tvWarningPhone.setText("전화번호를 확인해주세요");
            binding.tvWarningPhone.setVisibility(View.VISIBLE);
        }
    }

    public boolean isIdPattern(){
        Pattern id_pattern = Pattern.compile("^[a-z0-9]+$");

        Matcher matcher = id_pattern.matcher(binding.edtUserId.getText().toString());
        if(matcher.matches()){
            return true;
        }else{
            return false;
        }
    }


    public void idDupCheck(String guardian_id){
        CommonConn conn = new CommonConn("andidcheck",this);
        conn.addParamMap("guardian_id",guardian_id);

        conn.onExcute((isResult, data) -> {

            if(data.equals("0")){
                binding.btnIdCheck.setVisibility(View.GONE);
                binding.tvWarningId.setVisibility(View.GONE);
                binding.edtUserId.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.img_check,0);
            }else{
                binding.tvWarningId.setText("아이디 중복입니다");
                binding.tvWarningId.setVisibility(View.VISIBLE);
                binding.edtUserId.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
        });
    }

    public void bloodCheck() {

        binding.spnBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String blood = binding.spnBloodType.getSelectedItem().toString();
                Log.d("change", blood);
                if (blood.equals("혈액형")) {
                    binding.tvWarningBlood.setVisibility(View.VISIBLE);
                }else {
                    binding.tvWarningBlood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }*/
}