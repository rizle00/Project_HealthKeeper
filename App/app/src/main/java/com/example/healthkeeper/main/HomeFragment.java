package com.example.healthkeeper.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentHomeBinding;
import com.example.healthkeeper.main.cctv.CCTVActivity;
import com.example.healthkeeper.main.community.CommunityActivity;
import com.example.healthkeeper.main.monitor.ConditionActivity;
import com.gun0912.tedpermission.PermissionListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    public String fname=null;
    public String str=null;
    private AlertDialog alertDialog;
    FragmentHomeBinding binding;
    private final PermissionUtils permission = new PermissionUtils();

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

                    permission.checkCallPermission(callPermissionListener);
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
            SharedPreferences pref = this.getActivity().getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
            String name = pref.getString("user_name","익명");
            binding.edtUserName.setText(name+ "님");
//            pref.getString("role","") 보호자의 경우 환자의 이름 가져올까..?
        }
    private final PermissionListener callPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 권한 설정됨, 긴급전화 시행
            showDialogEmergency();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //권한 거부
            makeDenyDialog();

        }
    };

    private void makeDenyDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("권한 요청")
                .setMessage("권한이 반드시 필요합니다.!!미허용시 기능 사용 불가!")
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "권한 허용이 필요합니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                    permission.checkCallPermission(callPermissionListener);
                            }
                        })

                .setCancelable(true)
                .show();
    }
    }





