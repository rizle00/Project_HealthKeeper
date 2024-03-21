package com.example.healthkeeper.main;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthkeeper.App;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.ActivityPushBinding;
import com.example.healthkeeper.setting.HospitalActivity;
import com.example.healthkeeper.setting.SwipeDismissRecyclerViewTouchListener;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.List;

public class PushActivity extends AppCompatActivity {

    ActivityPushBinding binding;
    CommonRepository repository;
    CommonConn conn;
    SharedPreferences pref;
    private RecyclerView rev;
    private PushAdapter adapter; // 어댑터 객체 선언
    private List<AlarmLogVO> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPushBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = new CommonRepository(((App)getApplication()).executorService);

        pref = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
        loadAlarmLog();

    }

    private void loadAlarmLog() {
        conn =  new CommonConn("member/alarmLog");
        conn.addParamMap("params", pref.getString("user_id",""));
        repository.select(conn).thenAccept(result->{
            list = new Gson().fromJson(result,new TypeToken<List<AlarmLogVO>>(){}.getType() );
            if(!list.isEmpty()){
                createRev();
            }
        });


    }
    private void createRev() {
        rev = binding.recvPush;

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this);
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rev.setLayoutManager(horizontalLayoutManager);


        adapter = new PushAdapter(getLayoutInflater(), list, PushActivity.this);
        rev.setAdapter(adapter);

        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                rev,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view) {
                        int id = rev.getChildPosition(view);
                        adapter.mDataset.remove(id);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(getBaseContext(), String.format("Delete item %d",id),Toast.LENGTH_SHORT).show();
                    }
                }).setIsVertical(true).create();


        rev.setOnTouchListener(listener);
    }

    private void showAddDoctorDialog(String hospitalName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PushActivity.this);
        builder.setTitle(hospitalName + " - 의사 추가");

        // 의사 이름을 입력할 EditText 추가
        final EditText doctorInput = new EditText(PushActivity.this);
        doctorInput.setInputType(InputType.TYPE_CLASS_TEXT);
        doctorInput.setHint("의사 이름을 입력하세요.");
        builder.setView(doctorInput);

        // 확인 버튼 설정
        builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 입력된 의사 이름
                String doctorName = doctorInput.getText().toString().trim();
                adapter.notifyDataSetChanged();
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