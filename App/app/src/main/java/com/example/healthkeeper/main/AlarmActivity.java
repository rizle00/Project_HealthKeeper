package com.example.healthkeeper.main;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.bluetooth.BluetoothRepository;
import com.example.healthkeeper.databinding.ActivityAlarmBinding;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AlarmActivity extends AppCompatActivity {
    ActivityAlarmBinding binding;
    private final long timeCount = 60*1000;
    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private TextView tv_time;
    private Button btn_stop;
    private CountDownTimer countDownTimer;
    private SharedPreferences pref;
    private BluetoothRepository repository;
    private Executor executor;
    private String type, content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(!getIntent().getType().isEmpty()){ // 인텐트가 있을때만 시행
            type = getIntent().getType();
            content = getIntent().getStringExtra("content");
            initViews();
            startTimer();
        } else finish();

        btn_stop.setOnClickListener(view -> {
            stopTimer();
            finish();
        });
    }
    private void initViews() { // 초기화
        pref = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
        progressBarCircle =  binding.progressBarCircle;
        tv_time = binding.tvTime;
        btn_stop = binding.btnCloseAlarm;
        this.executor = ((App) getApplication()).executorService;
        initRepository();
    }
    private void initRepository() {
        repository = new BluetoothRepository(
                executor,
                this
        );
    }
    private void startTimer(){ // 타이머 시작
        if (timerStatus == TimerStatus.STOPPED) {


            setProgressBarValues();


            timerStatus = TimerStatus.STARTED;

            startCountDownTimer();}

    }

    private void setProgressBarValues() { // 프로그래스바 세팅

        progressBarCircle.setMax((int) timeCount / 1000);
        progressBarCircle.setProgress((int) timeCount / 1000);
    }
    private void stopTimer() { // 타이머 종료
        timerStatus = TimerStatus.STOPPED;

        countDownTimer.cancel();
        Toast.makeText(AlarmActivity.this,"알람이 종료되었습니다", Toast.LENGTH_SHORT).show();
    }
    private void startCountDownTimer() {// 타이머 시작

        countDownTimer = new CountDownTimer(timeCount, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                tv_time.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {// 타이머 종료시에만 시행

                tv_time.setText(hmsTimeFormatter(timeCount));

                setProgressBarValues();
                createPush();




                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
    }
    private void createPush() {// 푸시 생성, 및 알람로그 인서트 요청
        HashMap<String,Object> map = new HashMap<>();
//        map.put("token",pref.getString("token",""));
        String text = pref.getString("user_name","")+"님의"+content;
        map.put("guardian_id", pref.getString("guardian_id",""));
        map.put("title",type);
        map.put("body",text);

        repository.insertAlarm(map);
        SendSMS("01051760118",text);
    }
    public void SendSMS(String number, String msg){// 문자보내기
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number,null, msg,null,null);
    }

    private String hmsTimeFormatter(long milliSeconds) { // 타이머 포매팅
        long totalSeconds = milliSeconds / 1000; // 밀리초를 초로 변환
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;

        return String.format("%02d:%02d", minutes, seconds);

    }

}