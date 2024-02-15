package com.example.healthkeeper.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentHomeBinding;
import com.example.healthkeeper.member.LoginActivity;


public class HomeFragment extends Fragment {
    private AlertDialog alertDialog;
    FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);


        binding.case1.setOnClickListener(new View.OnClickListener() {//심박, 체온 현재상태보여줄 엑티비티
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ConditionActivity.class));
            }
        });

        binding.case2.setOnClickListener(new View.OnClickListener() {//최근 통계를 보여주는 엑티비티
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ChatActivity.class));
            }
        });

        binding.case3.setOnClickListener(new View.OnClickListener() {//긴급통화(119) 연결 다이얼로그
            @Override
            public void onClick(View v) {

                showDialogEmergency();
            }
        });


        binding.case4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(),CCTVActivity.class));
                    }
        });


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LoginActivity.class));

            }
        });



        return binding.getRoot();
    }



    private void showDialogEmergency() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_emergency, null);
            builder.setView(dialogView);

            TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
            TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
            Button confirmButton = dialogView.findViewById(R.id.confirmButton);
            Button cancelButton = dialogView.findViewById(R.id.cancelButton);


            dialogTitle.setText("긴급 전화");
            dialogMessage.setText("긴급 전화 연결됩니다.\n 연결하시겠습니까?");

            confirmButton.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent= new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:긴급전화번호"));
                    startActivity(callIntent);

                    alertDialog.dismiss();
                }
            }));

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();
                }
            });
            alertDialog = builder.create();

            alertDialog.show();
        }



    }


