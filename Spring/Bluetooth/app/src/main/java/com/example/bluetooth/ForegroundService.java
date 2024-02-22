package com.example.bluetooth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    private Thread mthread;
    private BtService btService;
    private void startForegroundService(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle("헬스 키퍼");
        builder.setContentText("헬스 키퍼 동작중");

        Intent notification = new Intent(this, BtService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_DEFAULT));
        }
        startForeground(1,builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if("startForeground".contains(intent.getAction())){
            startForegroundService();
        } else if (mthread == null) {
            mthread = new Thread("ServiceThread"){
                @Override
                public void run(){
                    while (true){
                        Log.i("디버깅", "스레드 동작중...");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                           e.printStackTrace();
                        }
                    }
                }
            };
            mthread.start();
        }
        Intent btServiceIntent = new Intent(this, BtService.class);
//        bindService(btServiceIntent, ,BIND_AUTO_CREATE);

        btService = new BtService();
        // 자동 연결 수행

        // START_STICKY를 반환하여 시스템이 서비스를 종료시킨 후에도 자동으로 재시작합니다.
        return START_STICKY;
    }

    private final ServiceConnection mServiceConnection() = new ServiceConnection(){

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
