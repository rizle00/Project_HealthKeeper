package com.example.healthkeeper.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class BluetoothService extends Service {
    private BluetoothManager bluetoothManager;

    IBinder mBinder = new serviceBinder();

    class serviceBinder extends Binder {
        BluetoothService getService() { // 서비스 객체를 리턴
            return BluetoothService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 블루투스 매니저 초기화
        bluetoothManager = new BluetoothManager(this, "BluetoothDeviceName");
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
