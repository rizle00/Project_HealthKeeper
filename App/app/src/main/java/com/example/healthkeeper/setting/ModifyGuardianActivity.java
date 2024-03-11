package com.example.healthkeeper.setting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityModifyGuardianBinding;
import com.example.healthkeeper.member.GuardianMemberVO;
import com.example.healthkeeper.member.JoinTypeActivity;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyGuardianActivity extends AppCompatActivity {
/*    ActivityModifyGuardianBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyGuardianBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnModify.setOnClickListener(v -> {
            joinClick();
            pwCheck();
            modify();
        });
    }

    *//*로그인 정보 가져오기*//*
    public void getPreference(){
       getSharedPreferences("PROJECT_MEMBER",MODE_PRIVATE).getString("user_id","회원");
        setContentView(R.layout.activity_modify_guardian);

    }

    public void modify(){
        CommonConn conn = new CommonConn("/andmodify",this);
        GuardianMemberVO vo = new GuardianMemberVO();

        vo.setGuardian_pw(binding.edtUserPw.getText().toString());
        vo.setGuardian_email(binding.edtUserEmail.getText().toString());
        vo.setGuardian_phone(binding.edtUserPhone.getText().toString());
        String voJson = new Gson().toJson(vo);
        conn.addParamMap("vo",voJson);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        conn.onExcute((isResult, data) -> {
            if(data.equals("1")){
                builder.setMessage("정보 수정이 완료되었습니다").show();
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }else{
                builder.setMessage("회원 정보 수정이 실패하였습니다 다시 시도해주세요").show();
            }
        });

    }

    public void joinClick(){
        mailPatterns();
        phonePattern();
        int num = binding.tvWarningId.getVisibility()+binding.tvWarningPw.getVisibility()
                + binding.tvWarningEmail.getVisibility()+binding.tvWarningPhone.getVisibility();
        if(num ==32){
            JoinTypeActivity jta = (JoinTypeActivity)JoinTypeActivity.joinTypeActivity;
            jta.finish();
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

    *//*비밀번호 일치, 글자수 확인*//*
    public void pwCheck() {
        String user_pw = binding.edtUserPw.getText().toString();
        if(!user_pw.equals(binding.edtUserPwCheck.getText().toString())){
            binding.tvWarningPw.setText("비밀번호가 일치하지 않습니다.");
            binding.tvWarningPw.setVisibility(View.VISIBLE);
        }else if(user_pw.length()<9){
            binding.tvWarningPw.setText("비밀번호를 8자 이상 입력해주세요");
            binding.tvWarningPw.setVisibility(View.VISIBLE);
        }
        else{
            binding.tvWarningPw.setVisibility(View.GONE);
        }
    }

    *//*비밀번호 영문, 숫자, 특문 정규식*//*
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
    }*/
}