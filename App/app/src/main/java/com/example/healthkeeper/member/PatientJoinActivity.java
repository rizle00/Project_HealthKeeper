package com.example.healthkeeper.member;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityPatientJoinBinding;

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

        /*혈액형 선택되었는지 확인*/
        bloodCheck();

        /* 아이디 글자수 확인 */
       IdLength();

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

        int num = binding.tvWarningBlood.getVisibility()+binding.tvWarningId.getVisibility()+binding.tvWarningPw.getVisibility();
        if(num ==24){
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정보가 올바르지 않습니다");
            builder.show();
        }

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

    public boolean pwCheck() {
        String user_pw = binding.edtUserPw.toString();
        if(user_pw.equals(binding.edtUserPwCheck)){
            binding.tvWarningPw.setVisibility(View.GONE);
            return true;
        }else{
            binding.tvWarningPw.setVisibility(View.VISIBLE);
            return false;
        }
    }
}