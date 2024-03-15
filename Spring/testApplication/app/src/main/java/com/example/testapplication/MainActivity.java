package com.example.testapplication;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import com.example.testapplication.common.CommonConn;
import com.example.testapplication.common.CommonRepository;
import com.example.testapplication.databinding.ActivityMainBinding;
import com.example.testapplication.firebase.ReqDTO;
import com.example.testapplication.firebase.RequestDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import lombok.Lombok;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private final long timeCount = 5*1000;
    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private TextView tv_time;
    private Button btn_stop;
    private CountDownTimer countDownTimer;
    private String type, content;
    private CommonConn commonConn;
    private CommonRepository repository;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private static String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();
        checkNotiPermission();
        initViews();
        startTimer();

        btn_stop.setOnClickListener(view -> {
//            stopTimer();
//            createPush();
            push();
        });

    }

    private void initViews() { // 초기화
        progressBarCircle =  binding.progressBarCircle;
        tv_time = binding.tvTime;
        btn_stop = binding.btnCloseAlarm;
        initRep();

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
        Log.d("TAG", "stopTimer: 종료됨");
        Toast.makeText(MainActivity.this,"알람이 종료되었습니다", Toast.LENGTH_SHORT).show();
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
//                createPush();
                push();


                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
    }

    private void push(){
        ReqDTO dto = new ReqDTO();
        // 타입, 멤버 id,네임?, 가디언
        dto.setMember_id("1");
        dto.setName("테스트");
        dto.setGuardian_id("2");
        dto.setCategory_id("7");
        repository.insertData("api/fcm", dto);
        createNoti();


    }
    private void createNotificationChannel() {
        Log.d("TAG", "createNotificationChannel: noti");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification channel";
            String description = "ground channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = null;


            channel = new NotificationChannel("noti", name, importance);

            channel.setDescription(description);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, "noti");
            builder.setSmallIcon(R.drawable.alarm)
                    .setContentTitle("Health Keeper");
        }
    }

    private void initRep() {
        repository = new CommonRepository(((App) getApplication()).executorService, this);
        HashMap<String, Object> map = new HashMap<>();
        map.put("key","asd");
    }
    private void createNoti(){
        Intent intent = new Intent(this, testActivity.class);
        intent.putExtra("id",  1001);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1001, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000});
        builder.setContentText("테스트입니당");
        notificationManager.notify(1001, builder.build());

    }

    private void createPush() {// 푸시 생성, 및 알람로그 인서트 요청
//        HashMap<String,Object> map = new HashMap<>();
//        map.put("token",pref.getString("token",""));
//        String text = pref.getString("user_name","")+"님의"+content;
        commonConn = new CommonConn("api/fcm", this);
        RequestDTO vo = new RequestDTO();
        type = " 낙상발생";
        String text = "ㅇ1234";
//        map.put("guardian_id", 1);
//        map.put("title",type);
//        map.put("body",text);
        Log.d("TAG", "onFinish: push ");
//        SendSMS("01051760118",text);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        test = token;


                    }
                });
        vo.setBody(text);
        vo.setTitle(type);
        vo.setGuardian_id("1");
        vo.setTargetToken(test);
        Log.d("TAG", "test: "+test);
      String json =  new Gson().toJson(vo);
        commonConn.addParamMap("params", json);
        commonConn.onExcute((isResult, data) -> {
            Log.d("TAG", "createPush: "+isResult);
        });

    }
    public void SendSMS(String number, String msg){

        SmsManager sms = SmsManager.getDefault();
        Log.d("TAG", "onFinish: sms "+sms);
        sms.sendTextMessage(number,null, msg,null,null);
    }

    private String hmsTimeFormatter(long milliSeconds) { // 타이머 포매팅
        long totalSeconds = milliSeconds / 1000; // 밀리초를 초로 변환
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;

        return String.format("%02d:%02d", minutes, seconds);


    }

    private void checkNotiPermission() {
        TedPermission.create()
                .setPermissionListener(notiPermissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(
                        android.Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.SEND_SMS
                )
                .check();
    }
    private final PermissionListener notiPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 권한 설정됨, 긴급전화 시행
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //권한 거부

        }
    };
}