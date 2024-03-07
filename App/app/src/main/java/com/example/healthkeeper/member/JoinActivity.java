package com.example.healthkeeper.member;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityJoinBinding;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    final String TAG = "guardian join";
    ActivityJoinBinding binding;



    private final int SEARCH_ADDRESS_ACTIVITY = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.edtAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PopupSearchAddressActivity.class);
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
        });
        /* 아이디 유효성 메소드 */
        usableIdCheck();
        /*아이디중복확인*/
        idDupCheck();
        /* 비밀번호 특문 메소드*/
        pwPattern();
        binding.btnJoin.setOnClickListener(v -> {
            pwCheck();
            joinClick();
        });
        binding.llPartner.setOnClickListener(v -> {
            partnerCheck();
        });
        if (!isPatient(getIntent().getStringExtra("type"))) {
            binding.llBloodtype.setVisibility(View.GONE);
        }

        /* 혈액형 */
        Spinner bloodTypeSpn = (Spinner) binding.spnBloodType;
        ArrayAdapter bloodAdapter = ArrayAdapter.createFromResource(this, R.array.bloodType, android.R.layout.simple_spinner_item);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bloodTypeSpn.setAdapter(bloodAdapter);

        binding.edtPatientId.setOnClickListener(v -> {
            binding.llPartner.performClick();
        });
        binding.btnGuardianFind.setOnClickListener(v -> {
            if(binding.btnGuardianFind.getText().toString().equals("취소")){
                binding.edtPatientId.setText("");
                binding.btnGuardianFind.setText("아이디 확인");
            }else {
                binding.llPartner.performClick();
            }
        });
    }


    public void addressCheck() {
        if (binding.edtAddress.getText().length() == 0 ||
                binding.edtAddressDetail.getText().length() == 0) {
            binding.tvWarningAddr.setVisibility(View.VISIBLE);
        }

    }


    /*다른 액티비티에서 정보를 받기위한 메소드*/
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
            idDupCheck(binding.edtUserId.getText().toString());
        });
        /* 아이디 중복체크 완료되면 체크표시 */

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

    public void joinClick() {
        usableIdCheck();
        mailPatterns();
        phonePattern();
        addressCheck();

        int num = binding.tvWarningId.getVisibility() + binding.tvWarningPw.getVisibility()
                + binding.tvWarningEmail.getVisibility() + binding.tvWarningPhone.getVisibility() + binding.tvWarningAddr.getVisibility();
        if (num == 40) {
            JoinTypeActivity jta = (JoinTypeActivity) JoinTypeActivity.joinTypeActivity;
            CommonConn conn = new CommonConn("andjoin", this);
            MemberVO vo = new MemberVO();
            vo.setMember_id(binding.edtUserId.getText().toString());
            vo.setPw(binding.edtUserPw.getText().toString());
            vo.setEmail(binding.edtAddress.getText().toString());
            vo.setPhone(binding.edtUserPhone.getText().toString());
            vo.setGuardian_id(binding.edtPatientId.getText().toString());
            vo.setName(binding.edtUserName.getText().toString());
            vo.setRole(getIntent().getStringExtra("type"));
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


    /*아이디 길이 확인*/
    public void usableIdCheck() {
        binding.tvWarningId.setText("아이디를 7~20자로 입력해주세요");
        binding.edtUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int idLength = binding.edtUserId.getText().length();
                if (idLength < 7 || idLength > 16) {
                    binding.tvWarningId.setText("아이디를 7~20자로 입력해주세요");
                    binding.tvWarningId.setVisibility(View.VISIBLE);
                    binding.btnIdCheck.setVisibility(View.GONE);
                } else if (!isIdPattern()) {
                    binding.tvWarningId.setText("영어 소문자와 숫자만 가능합니다");
                    binding.tvWarningId.setVisibility(View.VISIBLE);
                    binding.btnIdCheck.setVisibility(View.GONE);
                } else if (binding.edtUserId.getText().toString() == "") {
                    binding.tvWarningId.setText("아이디를 입력해주세요");
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

    public void pwPattern() {
        binding.edtUserPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isPwPattern()) {
                    binding.tvWarningPw.setText("영문,특수문자,숫자를 포함해야합니다");
                    binding.tvWarningPw.setVisibility(View.VISIBLE);
                } else {
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
        String user_pw = binding.edtUserPw.getText().toString();
        if (!user_pw.equals(binding.edtUserPwCheck.getText().toString())) {
            binding.tvWarningPw.setText("비밀번호가 일치하지 않습니다.");
            binding.tvWarningPw.setVisibility(View.VISIBLE);
        } else if (user_pw.length() < 9) {
            binding.tvWarningPw.setText("비밀번호를 8자 이상 입력해주세요");
            binding.tvWarningPw.setVisibility(View.VISIBLE);
        } else {
            binding.tvWarningPw.setVisibility(View.GONE);
        }
    }

    /*비밀번호 영문, 숫자, 특문 정규식*/
    public boolean isPwPattern() {
        Pattern pw_pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$");
        String pw = binding.edtUserPw.getText().toString();
        Matcher matcher = pw_pattern.matcher(pw);
        if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }

    public void mailPatterns() {
        Pattern mail_pattern = Patterns.EMAIL_ADDRESS;
        if (mail_pattern.matcher(binding.edtUserEmail.getText().toString()).matches()) {
            binding.tvWarningEmail.setVisibility(View.GONE);
        } else {
            binding.tvWarningEmail.setVisibility(View.VISIBLE);
        }
    }

    public void phonePattern() {
        Pattern phone_pattern = Patterns.PHONE;
        if (phone_pattern.matcher(binding.edtUserPhone.getText().toString()).matches()) {
            binding.tvWarningPhone.setVisibility(View.GONE);
        } else {
            binding.tvWarningPhone.setVisibility(View.VISIBLE);
        }
    }

    public boolean isIdPattern() {
        Pattern id_pattern = Pattern.compile("^[a-z0-9]+$");

        Matcher matcher = id_pattern.matcher(binding.edtUserId.getText().toString());
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public void partnerCheck() {
        CommonConn conn = new CommonConn("partnercheck", this);
        String partner;
        EditText edt = new EditText(this);
        if (isPatient(getIntent().getStringExtra("type"))) {
            partner = "보호자";
        } else {
            partner = "환자";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(partner + " 등록")
                .setMessage(partner+" 아이디를 입력해주세요")
                .setView(edt).setPositiveButton("등록하기", (dialog, which) -> {

                    conn.addParamMap("partner_id", edt.getText().toString());
                    conn.onExcute((isResult, data) -> {
                        if(data.equals("1")){
                            binding.edtPatientId.setText(edt.getText().toString());
                            binding.btnGuardianFind.setText("취소");
                        }else{
                            AlertDialog builder1 = new AlertDialog.Builder(this).setMessage("존재하지 않는 회원입니다").show();
                        }
                    });
                }).setNegativeButton("취소", (dialog, which) -> {
                    dialog.dismiss();
                });
        builder.show();

    }


    public void idDupCheck(String guardian_id) {
        CommonConn conn = new CommonConn("andidcheck", this);
        conn.addParamMap("guardian_id", guardian_id);

        conn.onExcute((isResult, data) -> {
            Log.i(TAG, "idDupCheck: " + data);

            if (data.equals("0")) {
                binding.btnIdCheck.setVisibility(View.GONE);
                binding.tvWarningId.setVisibility(View.GONE);
                binding.edtUserId.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_check, 0);
            } else {
                binding.tvWarningId.setText("아이디 중복입니다");
                binding.tvWarningId.setVisibility(View.VISIBLE);
                binding.edtUserId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            Log.i(TAG, "idDupCheck: " + data);
        });
    }

    public boolean isPatient(String type) {
        if (type.equals("patient")) {
            return true;
        } else {
            return false;
        }
    }
}