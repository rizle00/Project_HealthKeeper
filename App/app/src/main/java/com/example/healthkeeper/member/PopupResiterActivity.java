package com.example.healthkeeper.member;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityPopupResiterBinding;

public class PopupResiterActivity extends AppCompatActivity {
    ActivityPopupResiterBinding binding;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPopupResiterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(v -> {
            if(binding.tgbtn.getCheckedButtonId()==0){
                type="guardian";
            }else{
                type="patient"
            }
        if(binding.btnIdCheck.getVisibility()==View.GONE) {
            CommonConn conn = new CommonConn("partnerRegister", this);
            conn.addParamMap("partner", binding.edtPartnerId.getText().toString());
            conn.addParamMap("type", type);
            conn.onExcute((isResult, data) -> {
                if(data.equals("success")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("완료되었습니다").setPositiveButton("확인",(dialog, which) -> {
                       finish();
                    });
                }else{
                    Toast
                }
            }
        });
        };



        binding.btnIdCheck.setOnClickListener(v -> {
            CommonConn conn = new CommonConn("/partnercheck",this);
            conn.addParamMap("partner_id",binding.edtPartnerId.getText().toString());
            conn.onExcute((isResult, data) -> {
                if(data.equals("success")){
                    binding.btnIdCheck.setVisibility(View.GONE);
                    binding.edtPartnerId.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.img_check, 0);
                }else{
                    Toast.makeText(this, "존재하지 않는 아이디입니다", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}