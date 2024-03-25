package com.example.healthkeeper.firebase;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.bluetooth.BluetoothRepository;
import com.example.healthkeeper.databinding.ActivityAlarmBinding;
import com.example.healthkeeper.member.MemberVO;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class AlarmActivity extends AppCompatActivity {
    ActivityAlarmBinding binding;
    private final long timeCount = 10*1000;
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
    private String type, content, text;
    private MediaPlayer mediaPlayer;
//    NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(!getIntent().getType().isEmpty()){ // 인텐트가 있을때만 시행
            type = getIntent().getType();
            content = getIntent().getStringExtra("content");
            text = getIntent().getStringExtra("text");
            initViews();
            startTimer();
        } else this.finish();

        btn_stop.setOnClickListener(view -> {
            stopTimer();
            this.finish();

        });
    }
    private void initViews() { // 초기화
        pref = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
        progressBarCircle =  binding.progressBarCircle;
        tv_time = binding.tvTime;
        btn_stop = binding.btnCloseAlarm;
        this.executor = ((App) getApplication()).executorService;
        initRepository();
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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
    // 알람 소리 재생
    private void playAlarmSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm); // 알람 소리 파일을 raw 폴더에 추가해야 합니다.
            mediaPlayer.setLooping(true); // 반복 재생 설정
            mediaPlayer.start();
        }
    }

    // 알람 소리 중지
    private void stopAlarmSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void stopTimer() { // 타이머 종료
        timerStatus = TimerStatus.STOPPED;
        countDownTimer.cancel();
        stopAlarmSound();
//        notificationManager.cancel(getIntent().getIntExtra("notifyId",0));
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

                String guardian_id = pref.getString("guardian_id","");
                String name = pref.getString("user_name","");
                String address = pref.getString("address","");
                String json = pref.getString("guardian","");
                Log.d("TAG", "num: "+json);
                MemberVO vo = new Gson().fromJson(json,MemberVO.class);
                String number = vo.getPHONE().replace("-","");
                Log.d("TAG", "num: "+number);
                SendSMS(name,number, "\n119에 신고 되었습니다");
                SendSMS(name,"01051760118", "\n주소 : "+address);

                createPush(name,guardian_id);



                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        playAlarmSound();
    }
    private void createPush(String name, String id) {// 푸시 생성, 및 알람로그 인서트 요청

        RequestDTO dto = new RequestDTO();
        // 타입, 멤버 id,네임?, 가디언
        dto.setMEMBER_ID(pref.getString("user_id",""));
        dto.setName(name);
        dto.setGUARDIAN_ID(id);
        dto.setCATEGORY_ID(type);
        repository.insertAlarm(dto);
//        notificationManager.cancel(getIntent().getIntExtra("notifyId",0));

    }
    public void SendSMS(String name,String number, String content){// 문자보내기
        String msg = name+"님의"+text+content;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number,null, msg,null,null);
        Toast.makeText(AlarmActivity.this,"문자 신고가 발송되었습니다", Toast.LENGTH_SHORT).show();
    }

    private String hmsTimeFormatter(long milliSeconds) { // 타이머 포매팅
        long totalSeconds = milliSeconds / 1000; // 밀리초를 초로 변환
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;

        return String.format("%02d:%02d", minutes, seconds);

    }

}