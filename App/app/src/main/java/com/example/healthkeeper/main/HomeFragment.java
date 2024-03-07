package com.example.healthkeeper.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentHomeBinding;

import java.util.Map;


public class HomeFragment extends Fragment {
    public String fname=null;
    public String str=null;
    private AlertDialog alertDialog;
    FragmentHomeBinding binding;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        /*return binding.getRoot();*/
        View rootView = binding.getRoot();// 뷰 바인딩에서 루트 뷰를 가져와 rootView 변수에 저장


        if (getActivity() != null) {/* getActivity()가 null이 아닌 경우에만 작업 수행*/

            Intent intent = getActivity().getIntent();
            if (intent != null) {


                /*하단 setUserName(); 로 대체*/
//                String name = intent.getStringExtra("edt_user_name");//Intent에서 "edt_user_name" 키로 전달된 값을 가져옴
//                final String userID = intent.getStringExtra("edt_user_id");
//                binding.edtUserName.setText(name + "님");
            }

          /*  binding.btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginBeforeActivity.class));
                }
            });*/

            //메인화면 이름 호출
            setUserName();


            binding.case1.setOnClickListener(new View.OnClickListener() {//심박, 체온 현재상태보여줄 엑티비티
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), ConditionActivity.class));
                }
            });

            binding.case2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), CCTVActivity.class));
                }
            });

            binding.case3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogEmergency();
                }
            });

            binding.case4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), CommunityActivity.class));
                }
            });
        }
            return rootView;


        }

    private void changeFragment(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
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

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:긴급전화번호"));
                    startActivity(callIntent);
                    alertDialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();
                }
            });

            alertDialog = builder.create();
            alertDialog.show();
        }

        public void setUserName(){
            SharedPreferences pref = this.getActivity().getSharedPreferences("PROJECT_MEMBER",Context.MODE_PRIVATE);
            binding.edtUserName.setText(pref.getString("user_name","익명")+ "님");
        }

    }





