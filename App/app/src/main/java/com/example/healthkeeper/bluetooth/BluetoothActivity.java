package com.example.healthkeeper.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.healthkeeper.R;
import com.example.healthkeeper.bt.BTtestActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothService btService;
    private boolean mBound = false;

    // 서비스와의 연결을 처리하는 ServiceConnection 객체
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // 서비스와 연결되었을 때 호출됨
            BluetoothService.serviceBinder binder = (BluetoothService.serviceBinder) service;
            btService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // 서비스와 연결이 끊겼을 때 호출됨
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 서비스에 바인드
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 연결 해제
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

//    public void sendMessageToService(View view) {
//        if (mBound) {
//            // 서비스의 메서드 호출
//            int randomNumber = btService.getRandomNumber();
//            Toast.makeText(this, "Random Number from Service: " + randomNumber, Toast.LENGTH_SHORT).show();
//        }
//    }


}