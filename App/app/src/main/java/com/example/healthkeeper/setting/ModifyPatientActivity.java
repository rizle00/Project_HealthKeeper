package com.example.healthkeeper.setting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityModifyPatientBinding;
import com.example.healthkeeper.member.JoinTypeActivity;
import com.example.healthkeeper.member.MemberVO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyPatientActivity extends AppCompatActivity {
    ActivityModifyPatientBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnModify.setOnClickListener(v -> {
            joinClick();
            pwCheck();
        });

    }
    public void joinClick(){
        mailPatterns();
        phonePattern();
        int num = binding.tvWarningId.getVisibility()+binding.tvWarningPw.getVisibility()
                + binding.tvWarningPhone.getVisibility();
        if(num ==32){
            CommonConn conn = new CommonConn("membermodify",this);
            MemberVO vo = new MemberVO();
            vo.setPw(binding.edtUserPw.getText().toString());
            vo.setPhone(binding.edtUserPhone.getText().toString());
            vo.setName(binding.edtUserName.getText().toString());
            vo.setAddress(binding.edtAddress.getText().toString());
            vo.setAddress_detail(binding.edtAddressDetail.getText().toString());
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정보가 올바르지 않습니다");
            builder.show();
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

    public void mailPatterns(){
        Pattern mail_pattern = Patterns.EMAIL_ADDRESS;
        if(mail_pattern.matcher(binding.edtUserEmail.getText().toString()).matches()){
            binding.tvWarningEmail.setVisibility(View.GONE);
        }else {
            binding.tvWarningEmail.setVisibility(View.VISIBLE);
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