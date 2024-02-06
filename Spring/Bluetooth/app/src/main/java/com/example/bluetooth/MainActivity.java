package com.example.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BtService.OnCheckModelListener, BtService.OnNotifyValueListener<XiaomiSensor> {

    private TextView textView = null;
    private BtService btService;
    private boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // 서비스와 연결되었을 때 호출됩니다.
            BtService.LocalBinder binder = (BtService.LocalBinder) service;
            btService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // 서비스와 연결이 끊어졌을 때 호출됩니다.
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // 서비스에 바인딩합니다.
        Intent intent = new Intent(this, BtService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 서비스와 연결을 해제합니다.
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private final PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // Start Scan Device
            btService.scanDevices();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( btService.onActivityResult(requestCode, resultCode))
        {
            TedPermission.create()
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage("Denied Permission.")
                    .setPermissions(android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.ACCESS_FINE_LOCATION,  android.Manifest.permission.BLUETOOTH_CONNECT)
                    .check();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Add Listeners
        btService
                .setOnCheckModelListener( this)// 모델 체크 리스너
                .setOnNotifyValueListener( this);// 값 체크 리스너

        // 권한 체크
        if( btService.isOn() ) {
            TedPermission.create()
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage("Denied Permission.")
                    .setPermissions(android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.BLUETOOTH_CONNECT)
                    .check();
        }
        else {//블루투스 기능 활성 요청
            btService.on(this);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear the resources
        btService.disconnectGATTAll();
        btService.onComplete();
    }


    @Override
    public boolean isChecked(byte[] bytes) {// 원하는 디바이스 세팅
        return XiaomiSensor.isType(bytes);
    }

    @Override
    public void scannedDevice(ScanResult result) {
        // Start Connecting device.
        btService.connGATT(getApplicationContext(), result.getDevice());
    }

    @Override
    public void onValue(BluetoothDevice deivce, XiaomiSensor value) {
        // Show the data that is notified value
        textView.setText(String.valueOf(value.temperature));
    }

    @Override
    public XiaomiSensor formatter(BluetoothGattCharacteristic characteristic) {
        // Format the data
        Integer value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 0);
        Integer value2 = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1);
        float temperature = value * 0.01f;
        int humidity = (value2 & 0xFF00) >> 8;
        return new XiaomiSensor(
                System.currentTimeMillis(),
                temperature,
                humidity
        );
    }
}