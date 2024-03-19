package com.example.healthkeeper.setting;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthkeeper.R;

import java.util.ArrayList;
import java.util.List;

public class DiseaseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextDiseaseName, editTextDescription;
    private DiseaseAdapter adapter;
    private List<DiseaseVO> diseaseList;

    // 예시로 쉐어드 프리퍼런스에서 멤버 아이디를 가져오는 메서드
    private String getMemberIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        return sharedPreferences.getString("member_id", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        recyclerView = findViewById(R.id.recycler_view);
        editTextDiseaseName = findViewById(R.id.edit_text_disease_name);
        editTextDescription = findViewById(R.id.edit_text_description);

        // RecyclerView 초기화
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        diseaseList = new ArrayList<>();
        adapter = new DiseaseAdapter(this, diseaseList);
        recyclerView.setAdapter(adapter);

        // 질병 데이터 조회 및 업데이트
        updateDiseaseList();

        // 새 질병 추가 버튼 클릭 리스너
        findViewById(R.id.btn_add_disease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDisease();
            }
        });
    }

    // 질병 데이터 조회 및 업데이트
    private void updateDiseaseList() {
        // 여기에 질병 테이블에서 데이터를 조회하여 diseaseList 업데이트하는 코드 작성
        // 예시로, 더미 데이터로 리스트를 채우는 코드
        diseaseList.clear();
        diseaseList.add(new Disease(1, "Flu", "Influenza", "user_id_1")); // 예시 데이터
        adapter.notifyDataSetChanged();
    }

    // 질병 추가
    private void addDisease() {
        String name = editTextDiseaseName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String memberId = getMemberIdFromSharedPreferences();

        // 데이터 유효성 검사
        if (name.isEmpty() || description.isEmpty() || memberId.isEmpty()) {
            Toast.makeText(this, "Please enter disease name and description", Toast.LENGTH_SHORT).show();
            return;
        }

        // 여기에 질병을 추가하는 코드 작성

        // 추가 후 UI 업데이트
        updateDiseaseList();
    }

    // 질병 수정
    private void updateDisease(Disease disease) {
        // 여기에 질병을 수정하는 코드 작성

        // 수정 후 UI 업데이트
        updateDiseaseList();
    }

    // 질병 삭제
    private void deleteDisease(Disease disease) {
        // 여기에 질병을 삭제하는 코드 작성

        // 삭제 후 UI 업데이트
        updateDiseaseList();
    }

    // 리사이클러뷰 아이템 클릭 시 호출됨
    @Override
    public void onItemClick(int position) {
        // 질병 수정 또는 삭제 기능 구현
    }
}