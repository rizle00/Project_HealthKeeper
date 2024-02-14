package com.example.healthkeeper.bluetooth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.healthkeeper.R;

public class BluetoothService extends Service {
    private Thread thread;
    private BluetoothManager bluetoothManager;

    IBinder mBinder = new serviceBinder();

    class serviceBinder extends Binder {
        BluetoothService getService() { // 서비스 객체를 리턴
            return BluetoothService.this;
        }
    }

    private void startForegroundService(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle("아이키 키리스");
        builder.setContentText("키리스 동작중");

        Intent notification = new Intent(this, BluetoothActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification, 0);
        builder.setContentIntent(pendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_DEFAULT));
        }
        startForeground(1,builder.build());
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 블루투스 매니저 초기화
//        btService = new BluetoothManager(this, "BluetoothDeviceName");
        // 자동 연결 수행

        // START_STICKY를 반환하여 시스템이 서비스를 종료시킨 후에도 자동으로 재시작합니다.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 블루투스 연결을 종료합니다.
        bluetoothManager.off();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
