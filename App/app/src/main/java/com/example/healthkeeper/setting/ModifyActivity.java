package com.example.healthkeeper.setting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityModifyBinding;
import com.example.healthkeeper.member.MemberVO;
import com.example.healthkeeper.member.PopupSearchAddressActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyActivity extends AppCompatActivity {
    ActivityModifyBinding binding;
    private final int SEARCH_ADDRESS_ACTIVITY = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pwPattern();
        binding.btnModify.setOnClickListener(v -> {
            joinClick();
            pwCheck();
        });
        binding.edtAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PopupSearchAddressActivity.class);
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
        });

    }

    public void joinClick(){
        phonePattern();
        int num = binding.tvWarningId.getVisibility()+binding.tvWarningPw.getVisibility()
                + binding.tvWarningPhone.getVisibility();
        if(num ==24){
            CommonConn conn = new CommonConn("andmodify",this);
            MemberVO vo = new MemberVO();
            vo.setPw(binding.edtUserPw.getText().toString());
            vo.setPhone(binding.edtUserPhone.getText().toString());
            vo.setName(binding.edtUserName.getText().toString());
            vo.setAddress(binding.edtAddress.getText().toString());
            vo.setAddress_detail(binding.edtAddressDetail.getText().toString());
            vo.setMember_id(getSharedPreferences("PROJECT_MEMBER",MODE_PRIVATE).getString("user_id",""));
            String voJson = new Gson().toJson(vo);
            conn.addParamMap("vo",voJson);
            conn.onExcute((isResult, data) -> {
                if(data=="success"){
                    Toast.makeText(this,"회원정보 변경이 완료되었습니다",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this,"오류가 발생했습니다. 다시 시도해주세요",Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정보가 올바르지 않습니다");
            builder.show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        binding.edtAddress.setText(data);
                    }
                }
        }
    }
    public void pwPattern(){
        binding.edtUserPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isPwPattern()){
                    binding.tvWarningPw.setText("영문,특수문자,숫자를 포함해야합니다");
                    binding.tvWarningPw.setVisibility(View.VISIBLE);
                }else{
                    binding.tvWarningPw.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*비밀번호 일치, 글자수 확인*/
    public void pwCheck() {
        SharedPreferences pref = getSharedPreferences("PROJECT_MEMBER", Context.MODE_PRIVATE);
        String user_pw = binding.edtUserPw.getText().toString();
        if(!user_pw.equals(binding.edtUserPwCheck.getText().toString())){
            binding.tvWarningPw.setText("비밀번호가 일치하지 않습니다.");
            binding.tvWarningPw.setVisibility(View.VISIBLE);
        }else if(user_pw.length()<9&&pref.getString("user_name",null)!=null ){
            binding.tvWarningPw.setText("비밀번호를 8자 이상 입력해주세요");
            binding.tvWarningPw.setVisibility(View.VISIBLE);
        }
        else{
            binding.tvWarningPw.setVisibility(View.GONE);
        }
    }

    /*비밀번호 영문, 숫자, 특문 정규식*/
    public boolean isPwPattern(){
        Pattern pw_pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$");
        String pw = binding.edtUserPw.getText().toString();
        Matcher matcher = pw_pattern.matcher(pw);
        if(!matcher.matches()){
            return false;
        }else {
            return true;
        }
    }
    public void phonePattern(){
        Pattern phone_pattern = Patterns.PHONE;
        if(phone_pattern.matcher(binding.edtUserPhone.getText().toString()).matches()){
            binding.tvWarningPhone.setVisibility(View.GONE);
        }else{
            binding.tvWarningPhone.setVisibility(View.VISIBLE);
        }
    }


    public void socialCheck(String member_id){
        SharedPreferences pref = getSharedPreferences("PROJECT_MEMBER", Context.MODE_PRIVATE);
        CommonConn conn = new CommonConn("social",this);
        pref.getString("user_id","");
        if(pref.getString("user_name",null)==null){
            binding.edtUserPw.setVisibility(View.GONE);
            binding.edtUserPwCheck.setVisibility(View.GONE);
        }else{
            binding.edtUserPw.setVisibility(View.VISIBLE);
            binding.edtUserPwCheck.setVisibility(View.VISIBLE);
        }
    }


}