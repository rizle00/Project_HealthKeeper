package com.example.healthkeeper.member;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.healthkeeper.databinding.ActivityPatientJoinBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientJoinActivity extends AppCompatActivity {
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

        /*혈액형 선택되었는지 메소드*/
        bloodCheck();

        /* 아이디 글자수 메소드 */
       IdCheck();

       /* 비밀번호 특문 메소드*/
       pwPattern();
        binding.btnJoin.setOnClickListener(v -> {
            pwCheck();
            joinClick();
        });


        setContentView(binding.getRoot());
    }



    public void idCheck(){
        binding.btnIdCheck.setOnClickListener(v -> {

        });
        /* 아이디 중복체크 완료되면 체크표시 */
        binding.edtUserId.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.img_check,0);
    }

    public void joinClick(){

        int num = binding.tvWarningBlood.getVisibility()+binding.tvWarningId.getVisibility()+binding.tvWarningPw.getVisibility()
                ;
        if(num ==24){
            JoinTypeActivity jta = (JoinTypeActivity)JoinTypeActivity.joinTypeActivity;
            jta.finish();
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정보가 올바르지 않습니다");
            builder.show();
        }

    }

    /*아이디 길이 확인*/
    public void IdCheck(){
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
    /*혈액형 선택되었는지 확인*/
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
    }

    /*비밀번호 일치, 글자수 확인*/
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

    public boolean isMailPatterns(){
        Pattern mail_pattern = Patterns.EMAIL_ADDRESS;
        if(mail_pattern.matcher(binding.edtUserEmail.getText().toString()).matches()){
            return true;
        }else {
            return false;
        }
    }
    public boolean isPhonePattern(){
        Pattern phone_pattern = Patterns.PHONE;
        if(phone_pattern.matcher(binding.edtUserPhone.getText().toString()).matches()){

            return true;
        }else{
            return false;
        }
    }

    public boolean isIdPattern(){
        Pattern id_pattern = Pattern.compile("^[a-z0-9]+$");
        String id = binding.edtUserId.getText().toString();
        Matcher matcher = id_pattern.matcher(id);
        if(matcher.matches()){
            return true;
        }else{
            return false;
        }
    }
}