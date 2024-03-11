package com.example.healthkeeper.member;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.regex.Pattern;

public class SimpleJoinActivity extends AppCompatActivity {
    ActivitySimpleJoinBinding binding;
    private final int SEARCH_ADDRESS_ACTIVITY = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("joinintent", "onCreate: "+getIntent().getStringExtra("social"));
        binding = ActivitySimpleJoinBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        binding.edtAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PopupSearchAddressActivity.class);
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
        });
        CommonService apiInterface = CommonClient.getRetrofit().create(CommonService.class);
        HashMap<String, Object> params = new HashMap<>();

        binding.btnIdCheck.setOnClickListener(v -> {

        });

        usableIdCheck();

        /*아이디중복확인*/
        idDupCheck();


        binding.btnJoin.setOnClickListener(v -> {
            joinClick();
        });

        Spinner bloodTypeSpn = (Spinner) binding.spnBloodType;
        ArrayAdapter bloodAdapter = ArrayAdapter.createFromResource(this, R.array.bloodType, android.R.layout.simple_spinner_item);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bloodTypeSpn.setAdapter(bloodAdapter);

        bloodCheck();

        if (!isPatient(getIntent().getStringExtra("type"))) {
            binding.llBloodtype.setVisibility(View.GONE);
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

    public void idDupCheck() {
        binding.btnIdCheck.setOnClickListener(v -> {
            idDupCheck(binding.edtEmail.getText().toString());
        });

    }

    public boolean isPatient(String type) {
        if (type.equals("patient")) {
            return true;
        } else {
            return false;
        }
    }

    public void joinClick() {
        usableIdCheck();
        phonePattern();


        int num = binding.tvWarningId.getVisibility() + binding.tvWarningBlood.getVisibility()
                + binding.tvWarningPhone.getVisibility() + binding.tvWarningGender.getVisibility();
        if (num == 32) {


            JoinTypeActivity jta = (JoinTypeActivity) JoinTypeActivity.joinTypeActivity;
            CommonConn conn = new CommonConn("andjoin", this);
            MemberVO vo = new MemberVO();
            vo.setEmail(binding.edtEmail.getText().toString());
            vo.setPhone(binding.edtUserPhone.getText().toString());
            vo.setGuardian_id(binding.edtGuardianId.getText().toString());
            vo.setName(binding.edtUserName.getText().toString());
            vo.setSocial(getIntent().getStringExtra("social").toString());
            vo.setAddress(binding.edtAddress.getText().toString());
            vo.setAddress_detail(binding.edtAddressDetail.getText().toString());
            vo.setBlood(binding.spnBloodType.getSelectedItem().toString());
            if (binding.rgFemale.isChecked()) {
                vo.setGender("Female");
            } else {
                vo.setGender("Male");
            }
            String voJson = new Gson().toJson(vo);
            conn.addParamMap("vo", voJson);


            conn.onExcute((isResult, data) -> {

            });


            jta.finish();
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("정보가 올바르지 않습니다");
            builder.show();
        }
    }

    public void usableIdCheck() {
        binding.tvWarningId.setText("아이디를 7~20자로 입력해주세요");
        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int idLength = binding.edtEmail.getText().length();
                if (!isIdPatterns()) {
                    binding.tvWarningId.setText("이메일 형식으로 입력해주세요");
                    binding.tvWarningId.setVisibility(View.VISIBLE);
                    binding.btnIdCheck.setVisibility(View.GONE);
                } else if (binding.edtEmail.getText().toString() == "") {
                    binding.tvWarningId.setText("아이디(이메일)를 입력해주세요");
                    binding.tvWarningId.setVisibility(View.VISIBLE);
                    binding.btnIdCheck.setVisibility(View.GONE);
                } else {
                    binding.tvWarningId.setVisibility(View.GONE);
                    binding.btnIdCheck.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean isIdPatterns() {
        Pattern mail_pattern = Patterns.EMAIL_ADDRESS;
        if (mail_pattern.matcher(binding.edtEmail.getText().toString()).matches()) {
            return true;
        } else {
            return false;
        }
    }


    public void phonePattern() {
        Pattern phone_pattern = Pattern.compile("\\d{3}-\\d{3,4}-\\d{4}");
        if (phone_pattern.matcher(binding.edtUserPhone.getText().toString()).matches()) {
            binding.tvWarningPhone.setVisibility(View.GONE);
        } else {
            binding.tvWarningPhone.setText("전화번호를 확인해주세요");
            binding.tvWarningPhone.setVisibility(View.VISIBLE);
        }
    }


    public void idDupCheck(String guardian_id) {
        CommonConn conn = new CommonConn("andidcheck", this);
        conn.addParamMap("guardian_id", guardian_id);

        conn.onExcute((isResult, data) -> {

            if (data.equals("0")) {
                binding.btnIdCheck.setVisibility(View.GONE);
                binding.tvWarningId.setVisibility(View.GONE);
                binding.edtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_check, 0);
            } else {
                binding.tvWarningId.setText("아이디 중복입니다");
                binding.tvWarningId.setVisibility(View.VISIBLE);
                binding.edtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        });
    }

    public void bloodCheck() {

        binding.spnBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String blood = binding.spnBloodType.getSelectedItem().toString();
                if (isPatient(getIntent().getStringExtra("type")) && blood.equals("혈액형")) {
                    binding.tvWarningBlood.setVisibility(View.VISIBLE);
                } else {
                    binding.tvWarningBlood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}