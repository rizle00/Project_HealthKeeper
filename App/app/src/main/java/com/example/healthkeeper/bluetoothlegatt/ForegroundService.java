package com.example.healthkeeper.bluetoothlegatt;

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
import com.example.healthkeeper.bluetooth.BluetoothActivity;
import com.example.healthkeeper.bluetooth.BluetoothManager;
import com.example.healthkeeper.bluetooth.BluetoothService;

public class ForegroundService extends Service {

    private Thread thread;
    private BluetoothLeService mbluetoothLeService;


    private void startForegroundService(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle("아이키 키리스");
        builder.setContentText("키리스 동작중");

        Intent notification = new Intent(this, DeviceControlActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_DEFAULT));
        }
        startForeground(1,builder.build());
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
