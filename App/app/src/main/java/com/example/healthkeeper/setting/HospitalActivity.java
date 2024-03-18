package com.example.healthkeeper.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.ActivityHospitalBinding;
import com.example.healthkeeper.main.MainActivity;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HospitalActivity extends AppCompatActivity {
 ActivityHospitalBinding binding;
    private EditText edt_hospital;
    private Spinner hospitalSpinner;
    private Button btn_search, btn_addDoc, btn_insert;
    CommonRepository repository;
    SharedPreferences pref;

    MemberHospitalVO vo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        edt_hospital = binding.edtHospital;
        btn_search = binding.btnSearchHospital;
        hospitalSpinner = binding.spinnerHospital;
        btn_insert = binding.btnInsertHospital;
        btn_addDoc = binding.btnAddDoctor;
        repository = new CommonRepository(((App)getApplication()).executorService);
        vo =  new MemberHospitalVO();
        pref = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
        vo.setMEMBER_ID(pref.getString("user_id",""));
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 병원 이름을 가져옴
                String hospitalName = edt_hospital.getText().toString().trim();
                Log.d("TAG", "onClick: "+hospitalName);
                // 서버에서 병원 목록을 가져와서 스피너에 표시하는 메서드 호출
                fetchHospitalListFromServer(hospitalName);
            }
        });

        hospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 사용자가 선택한 병원 이름을 가져옴
                String selectedHospital = parent.getItemAtPosition(position).toString();
                // 의사 이름을 입력하는 다이얼로그를 표시
                showAddDoctorDialog(HospitalActivity.this, selectedHospital);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchHospitalListFromServer(String hospitalName) {
        // 여기서 서버에서 병원 목록을 가져와야 합니다.
        // 이 예제에서는 임의의 데이터를 사용합니다.
        CommonConn conn =  new CommonConn("member/hospital");
        conn.addParamMap("name", hospitalName);
        repository.select(conn).thenAccept(result->{
            List<HospitalVO> list = new Gson().fromJson(result,new TypeToken<List<HospitalVO>>(){}.getType() );
            // 병원 이름을 담을 새로운 리스트 생성
            List<String> hospitals = new ArrayList<>();

// 각 HospitalVO 객체에서 이름 값을 추출하여 새로운 리스트에 추가
            for (HospitalVO hospital : list) {
                hospitals.add(hospital.getNAME());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hospitals);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hospitalSpinner.setAdapter(adapter);
            hospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedHospital = hospitals.get(i);
                   vo.setHOSPITAL_ID(list.get(i).getHOSPITAL_ID());
                    edt_hospital.setText(selectedHospital);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    edt_hospital.setText("");
                }
            });

        });




    }

    private void showAddDoctorDialog(Context context, String hospitalName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(hospitalName + " - 의사 추가");

        // 의사 이름을 입력할 EditText 추가
        final EditText doctorInput = new EditText(context);
        doctorInput.setInputType(InputType.TYPE_CLASS_TEXT);
        doctorInput.setHint("의사 이름을 입력하세요.");
        builder.setView(doctorInput);

        // 확인 버튼 설정
        builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 입력된 의사 이름
                String doctorName = doctorInput.getText().toString().trim();

                // 여기서 입력된 의사 이름을 사용하여 작업을 수행합니다.
                // 예를 들어, 데이터베이스에 새로운 의사 정보를 추가하는 등의 작업을 수행할 수 있습니다.
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
}