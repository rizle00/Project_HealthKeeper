package com.example.testapplication2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.companion.CompanionDeviceManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.clj.fastble.BleManager;
import com.example.testapplication2.databinding.ActivityMainBinding;
import com.example.testapplication2.databinding.ActivityTestBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    private final static String TAG = TestActivity.class.getSimpleName();
    public static final int INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701;
    ActivityTestBinding binding;// 바인딩  처리
    private MyService mBluetoothLeService;
    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent gattServiceIntent = new Intent(this, MyService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        startService(gattServiceIntent);
    }

    //  블루투스 커넥트 요청시
    public void bluetooth(View view) {
        // 권한 체크
        if (isOn()) {
            checkPermission();

        } else {//블루투스 기능 활성 요청
            requestBluetoothActivation(this);

        }
    }

    // 블루투스 활성 체크
    public boolean isOn() {
        return adapter.isEnabled();
    }

    // Bluetooth 활성화 요청
    @SuppressLint("MissingPermission")
    public void requestBluetoothActivation(AppCompatActivity activity) {
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, INTENT_REQUEST_BLUETOOTH_ENABLE);
        }
    }

    // 활성 확인 시 권한체크, 취소 시 알림
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_BLUETOOTH_ENABLE && resultCode == RESULT_OK) {
            checkPermission();
        } else if (requestCode == INTENT_REQUEST_BLUETOOTH_ENABLE && resultCode == RESULT_CANCELED) {
            Toast.makeText(TestActivity.this, "블루투스 활성화가 필요합니다", Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    // 권한체크
    private void checkPermission() {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
                .check();
    }

    //  권한 체크 리스너 등록
    private final PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 권한 설정됨
            mBluetoothLeService = new MyService();
            if (!mBluetoothLeService.initialize(TestActivity.this)) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            Log.d(TAG, "onServiceConnected: " + 12345);
//            mBluetoothLeService.connect(mDeviceAddress);

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //권한 거부
            makeDenyDialog();

        }
    };

    private void makeDenyDialog() {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("권한 요청")
                .setMessage("권한이 반드시 필요합니다.!!미허용시 앱 사용 불가!")
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                .setPositiveButton("설정",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPermission();
                            }
                        })

                .setCancelable(true)
                .show();
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((MyService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize(TestActivity.this)) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            Log.d(TAG, "onServiceConnected: " + 12345);
//            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
}