package com.example.healthkeeper.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.FragmentSettingBinding;
import com.example.healthkeeper.main.MainActivity;
import com.example.healthkeeper.member.PopupResiterActivity;
import com.google.gson.Gson;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    CommonRepository repository;
    SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(getLayoutInflater());
        repository = new CommonRepository(((App) requireActivity().getApplication()).executorService);
        pref = getActivity().getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);

        binding.llMemberModify.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ModifyActivity.class);
            startActivity(intent);
        });

        binding.llMemberRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PopupResiterActivity.class);
            startActivity(intent);
        });

        binding.llDiseaseRegister.setOnClickListener(v -> {
            showAdddiseaseDialog(getContext());
        });

        binding.llHospitalRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HospitalActivity.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }

    // 입력 다이얼로그를 표시하는 메서드
    private void showAdddiseaseDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("새로운 질병 추가");

        // 다이얼로그 레이아웃 XML 파일을 인플레이트하여 뷰 객체로 가져옴
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_disease, null);
        builder.setView(dialogView);

        // EditText 가져오기
        final EditText editTextName = dialogView.findViewById(R.id.edit_text_item_name);
        final EditText editTextDescription = dialogView.findViewById(R.id.edit_text_item_description);

        // 확인 버튼 설정
        builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // EditText에서 입력값 가져오기
                String name = editTextName.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                DiseaseVO vo = new DiseaseVO();
                vo.setMEMBER_ID( pref.getString("user_id", ""));
                vo.setDISEASE_NAME(name);
                vo.setDESCRIPTION(description);
                // 여기서 입력값을 처리하거나 저장합니다.
                CommonConn conn = new CommonConn("member/disease");

               conn.addParamMap("params", new Gson().toJson(vo));
                Log.d("TAG", "onClick: "+vo.getDISEASE_NAME());
                repository.insert(conn).thenAccept(result->{
                    if(!result.isEmpty())
                        Toast.makeText(getContext(), "등록이 완료되었습니다", Toast.LENGTH_SHORT).show();
                });

            }
        });

        // 취소 버튼 설정
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 다이얼로그를 닫을 때 추가적인 작업이 필요한 경우 여기에 작성합니다.
                dialog.cancel();
            }
        });

        // 다이얼로그 표시
        builder.show();
    }

//    // 병원 목록을 조회하여 스피너에 넣고 의사 이름을 입력하는 다이얼로그를 표시하는 메서드
//    private void showAddDoctorDialog(Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("병원 / 의사 추가");
//
//        // 병원 이름을 입력할 EditText 추가
//        final EditText hospitalInput = new EditText(context);
//        hospitalInput.setInputType(InputType.TYPE_CLASS_TEXT);
//        hospitalInput.setHint("병원 이름을 입력하세요.");
//        builder.setView(hospitalInput);
//        Spinner spinner = new Spinner(context);
////        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, hospitalList);
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        spinner.setAdapter(adapter);
//        builder.setView(spinner);
//
//        // 의사 이름을 입력할 EditText 추가
//        final EditText doctorInput = new EditText(context);
//        doctorInput.setInputType(InputType.TYPE_CLASS_TEXT);
//        doctorInput.setHint("의사 이름을 입력하세요.");
//        builder.setView(doctorInput);
//
//        // 확인 버튼 설정
//        builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 입력된 병원 이름과 의사 이름
//                String hospitalName = hospitalInput.getText().toString().trim();
//                String doctorName = doctorInput.getText().toString().trim();
//
//                // 여기서 병원 이름을 사용하여 서버에서 병원에 속한 의사 목록을 조회합니다.
//                // 조회 결과를 기반으로 스피너를 초기화합니다.
//                List<String> doctorList = getDoctorListFromServer(hospitalName);
//
//                if (doctorList != null && !doctorList.isEmpty()) {
//                    // 서버에서 의사 목록을 성공적으로 가져왔을 경우
//                    // 스피너에 의사 목록을 설정합니다.
//                    showDoctorSelectionDialog(MainActivity.this, doctorList, hospitalName, doctorName);
//                } else {
//                    // 서버에서 의사 목록을 가져오지 못했을 경우
//                    Toast.makeText(MainActivity.this, "서버에서 의사 목록을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // 취소 버튼 설정
//        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 다이얼로그를 닫을 때 추가적인 작업이 필요한 경우 여기에 작성합니다.
//                dialog.cancel();
//            }
//        });
//
//        // 다이얼로그 표시
//        builder.show();
//    }

    // 서버에서 병원에 속한 의사 목록을 조회하는 메서드 (임시로 가정한 메서드입니다)
    private List<String> getDoctorListFromServer(String hospitalName) {
        // 여기서는 임시로 가짜 의사 목록을 반환합니다.
        List<String> doctorList = new ArrayList<>();
        doctorList.add("의사 1");
        doctorList.add("의사 2");
        doctorList.add("의사 3");
        return doctorList;
    }
}

