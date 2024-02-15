package com.example.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.example.test.databinding.ActivityMainBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701;
    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5*1000)
                .setConnectOverTime(20*1000)
                .setOperateTimeout(5*1000);
        // 권한 체크
        if(isOn() ) {
            checkPermission();
        }
        else {//블루투스 기능 활성 요청
            requestBluetoothActivation(this);

        }

    }

    private void checkPermission() {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_SCAN)
                .check();
    }

    // 블루투스 활성 체크
    public boolean isOn()
    {
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
//  권한 체크 리스너 등록
    private final PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 권한 설정됨
            startScan();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //권한 거부
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
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   checkPermission();
                                }
                            })

                    .setCancelable(false)
                    .show();

        }
    };

    // 활성 확인 시 권한체크
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_BLUETOOTH_ENABLE && resultCode == RESULT_OK) {
            checkPermission();
        }
    }




    private void startScan() {
        // 기기이름 배열로 추가 가능, 맥주소 등  세팅가능
        setScanRule("HM10");
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
            }

//            @Override
//            public void onLeScan(BleDevice bleDevice) {
//                super.onLeScan(bleDevice);
//            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                if (bleDevice.getName().equals("HM10")) {
                    BleManager.getInstance().cancelScan();
                }
            }

                @Override
                public void onScanFinished(List<BleDevice> scanResultList) {
                    showDeviceList(scanResultList);
                }
        });
    }

    private void setScanRule(String name) {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setDeviceName(true, name)   // 지정된 브로드캐스트 이름의 장치만 스캔합니다. 선택 사항입니다. 배열가능
//                .setDeviceMac(mac)                  // 지정된 MAC 주소의 장치만 스캔합니다. 선택 사항입니다.
                .setAutoConnect(true)      // 연결 시 autoConnect 매개변수입니다. 선택 사항입니다. 기본값은 false입니다.
                .setScanTimeOut(10*1000)              // 스캔 타임아웃 시간입니다. 선택 사항입니다. 기본값은 10초입니다.
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void showDeviceList(List<BleDevice> scanResultList){
        if (scanResultList.size() == 0) { // 스캔된 장치가 없는 경우.
            Toast.makeText(MainActivity.this, "근처에 연결 가능한 장치가 없습니다. 다시 시도하십시오.", Toast.LENGTH_LONG).show();
            startScan();
        }
        // 스캔된 장치가 있는 경우.
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("블루투스 장치 선택");

        // 각 디바이스는 이름과(서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다.
        List<String> listItems = new ArrayList<String>();

        //페어링된 기기 쿼리
        for (BleDevice device : scanResultList) {
            // device.getName() : 단말기의 Bluetooth Adapter 이름을 반환.
            listItems.add(device.getName());
        }

        listItems.add("취소");  // 취소 항목 추가.

        // CharSequence : 변경 가능한 문자열.
        // toArray : List형태로 넘어온것 배열로 바꿔서 처리하기 위한 toArray() 함수.
        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
        // toArray 함수를 이용해서 size만큼 배열이 생성 되었다.
        listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == scanResultList.size()) { // 연결할 장치를 선택하지 않고 '취소' 를 누른 경우.
                    Toast.makeText(MainActivity.this, "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    //                    finish();
                } else { // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함.
                    BleDevice selectedDevice = null;
                    for (BleDevice device : scanResultList) {
                        if (device.getName().equals(items[item])) {
                            selectedDevice = device;
                            break;
                        }
                    }
                    if (selectedDevice != null) {
                        connect(selectedDevice);
                    }

                }
            }

        });

        builder.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

            @Override
            public void onStartConnect() {


                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "장치 연결에 실패하였습니다", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "기기가 연결 되었습니다", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();


                if (isActiveDisConnected) {//사용자의 의한
                    Toast.makeText(MainActivity.this, "연결이 해제되었습니다", Toast.LENGTH_LONG).show();
                } else {// 의도치 않은
                    Toast.makeText(MainActivity.this, "연결이 해제되었습니다", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}