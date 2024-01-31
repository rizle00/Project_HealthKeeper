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
import com.example.healthkeeper.databinding.ActivityMemberJoinBinding;

public class MemberJoinActivity extends AppCompatActivity {
    MemberVO vo = new MemberVO();
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

        /*혈액형 선택되었는지 확인*/
        bloodCheck();

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

    public void joinType(){
        binding.rgUserGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int userGroup = binding.rgUserGroup.getCheckedRadioButtonId();
            if(userGroup==0){
                Log.d("userType", "환자 ");
                binding.spnBloodType.setVisibility(View.VISIBLE);

            }else{
                Log.d("userType", "보호자 ");
                binding.spnBloodType.setVisibility(View.GONE);
            }
        });
        binding.rdbGuardian.setOnClickListener(v -> {

        });
    }
}