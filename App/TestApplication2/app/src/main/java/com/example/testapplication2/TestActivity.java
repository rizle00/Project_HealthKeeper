package com.example.testapplication2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.*;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import bluetooth.BTManger;
import com.example.testapplication2.databinding.ActivityTestBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    private final static String TAG = TestActivity.class.getSimpleName();
    public static final int INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701;
    ActivityTestBinding binding;// 바인딩  처리
    private MyService mBluetoothLeService;
    private boolean mBound;
    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private int heart, accident;
    private double temp;

    private BTManger mbtManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mbtManger = new BTManger();

        btSwitch();
    }


    //  블루투스 커넥트 요청시 , 서비스의 온오프
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void btSwitch() {
        Switch toggleSwitch = binding.toggleSwitch;

        // 스위치 상태 변경 이벤트 리스너 설정
        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 상태 변경 시 호출되는 메서드
                if (isChecked) {
                    // 스위치가 켜진 경우
                    // 권한 체크

                    if (isOn()) {
                        checkPermission();

                    } else {//블루투스 기능 활성 요청
                        requestBluetoothActivation(TestActivity.this);

                    }
                } else {
                    if (mBound) {
                        mBluetoothLeService.disconnect();
                        unbindService(mServiceConnection);
                        mBluetoothLeService = null;
                        unregisterReceiver(mGattUpdateReceiver);
                        mBound = false;
                    }
                }
            }
        });
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
            // 권한 설정됨, 블루투스 초기화 시행
            Intent intent = new Intent(TestActivity.this, MyService.class);
            bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
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
                                Toast.makeText(TestActivity.this, "권한 허용이 필요합니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setPositiveButton("확인",
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter(), RECEIVER_EXPORTED);
            }
// 리시버 제대로 등록 ... 필수 암시적은 잘 등록 안되기도
            if (!mBluetoothLeService.initialize(TestActivity.this)) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                Toast.makeText(TestActivity.this, "블루투스가 작동하지 않습니다", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: " + action);
            if (MyService.ACTION_GATT_CONNECTED.equals(action)) {
                mBound = true;
                Log.d(TAG, "onReceive: " + action);
            } else if (MyService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mBound = false;
            } else if (MyService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                Log.d(TAG, "onReceive: " + action);
            } else if (MyService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, "onReceive: " + action);
                heart = Integer.parseInt(intent.getStringExtra("heart"));
                temp = Double.parseDouble(intent.getStringExtra("temp"));
                accident = Integer.parseInt(intent.getStringExtra("accident"));
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BtService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BtService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BtService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BtService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}