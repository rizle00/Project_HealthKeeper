package com.example.testapplication2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.*;
import android.companion.AssociationInfo;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.MacAddress;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.os.HandlerCompat;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;
import com.example.testapplication2.databinding.ActivityMainBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    public static final int INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701;
    private static final int SELECT_DEVICE_REQUEST_CODE = 0x012;
    private static final int INTENT_REQUEST_PERMISSION = 1000;
    private static final String TAG = "asd";
    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private List<BleDevice> deviceList;

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5 * 1000)
                .setConnectOverTime(20 * 1000)
                .setOperateTimeout(5 * 1000);
    }

    public void bluetooth(View view) {
        // 권한 체크
        if (isOn()) {
            checkPermission();

        } else {//블루투스 기능 활성 요청
            requestBluetoothActivation(this);

        }
    }

    private void checkPermission() {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
                .check();
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
                    .setPositiveButton("설정",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                                    startActivityForResult(intent, INTENT_REQUEST_PERMISSION);
                                    checkPermission();
                                }
                            })

                    .setCancelable(true)
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
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == SELECT_DEVICE_REQUEST_CODE && data != null) {
            BluetoothDevice deviceToPair =
                    data.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE);
            if (deviceToPair != null) {
                deviceToPair.createBond();
                // Continue to interact with the paired device.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void startScan() {
        // 기기이름 배열로 추가 가능, 맥주소 등  세팅가능
        setScanRule("HM10");
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Toast.makeText(MainActivity.this, "스캔을 시작합니다", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {

                if (bleDevice.getName().equals("HM10")) {
                    BleManager.getInstance().cancelScan();
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                deviceList = scanResultList;
                showDeviceList(scanResultList);
            }
        });
    }

    private void setScanRule(String name) {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setDeviceName(true, name)   // 지정된 브로드캐스트 이름의 장치만 스캔합니다. 선택 사항입니다. 배열가능
//                .setDeviceMac(mac)                  // 지정된 MAC 주소의 장치만 스캔합니다. 선택 사항입니다.50:65:83:09:4D:66
                .setAutoConnect(true)      // 연결 시 autoConnect 매개변수입니다. 선택 사항입니다. 기본값은 false입니다.
                .setScanTimeOut(10 * 1000)              // 스캔 타임아웃 시간입니다. 선택 사항입니다. 기본값은 10초입니다.
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void showDeviceList(List<BleDevice> scanResultList) {
        if (scanResultList.size() == 0) { // 스캔된 장치가 없는 경우.
            Toast.makeText(MainActivity.this, "근처에 연결 가능한 장치가 없습니다. 재검색을 시도합니다.", Toast.LENGTH_LONG).show();

            startScan();
        } else {// 스캔된 장치가 있는 경우.

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("블루투스 장치 선택");

            // 각 디바이스는 이름과(서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다.
            List<String> listItems = new ArrayList<String>();

            //페어링된 기기 쿼리
            for (BleDevice device : scanResultList) {
                // device.getName() : 단말기의 Bluetooth Adapter 이름을 반환.
                listItems.add(device.getName());
                deviceList.add(device);
            }

            listItems.add("취소");  // 취소 항목 추가.

            // CharSequence : 변경 가능한 문자열.
            // toArray : List형태로 넘어온것 배열로 바꿔서 처리하기 위한 toArray() 함수.
            final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
            // toArray 함수를 이용해서 size만큼 배열이 생성 되었다.
            listItems.toArray(new CharSequence[listItems.size()]);

            builder.setItems(items, new DialogInterface.OnClickListener() {

                @SuppressLint("MissingPermission")
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
                           BluetoothDevice device = selectedDevice.getDevice();
                            final Intent intent = new Intent(MainActivity.this, DeviceControlActivity.class);
                            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
                            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                            startActivity(intent);
                            Log.d(TAG, "onClick: "+intent);
                        }

                    }
                }

            });

            builder.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

//    @SuppressLint("MissingPermission")
//    private void congatt(final BluetoothDevice device) {
//        device.connectGatt(MainActivity.this, true, gattCallback);
//    }

//    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
//        @SuppressLint("MissingPermission")      // 연결 상태 변경 처리
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            super.onConnectionStateChange(gatt, status, newState);
//            if (status == BluetoothGatt.GATT_FAILURE) {
////                Toast.makeText(MainActivity.this, "장치 연결에 실패하였습니다", Toast.LENGTH_LONG).show();
//                gatt.disconnect();
//                gatt.close();
//
//                return;
//            }
//            if (status == 133) // Unknown Error
//            {
////                Toast.makeText(MainActivity.this, "장치 연결에 실패하였습니다", Toast.LENGTH_LONG).show();
//                gatt.disconnect();
//                gatt.close();
//                return;
//            }
//            if (newState == BluetoothGatt.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS) {
//                // "Connected to " + gatt.getDevice().getName()
////                Toast.makeText(MainActivity.this, "기기가 연결 되었습니다", Toast.LENGTH_LONG).show();
//                gatt.discoverServices();
//                Log.d(TAG, "onConnectionStateChange: "+gatt.getServices());
//            }
//
//        }
//
//        // 서비스 검색 완료 처리
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            super.onServicesDiscovered(gatt, status);
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                List<BluetoothGattService> services = gatt.getServices();
//                Log.d(TAG, "onServicesDiscovered: "+services);
//                for (BluetoothGattService service : services) {
//                    // "Found service : " + service.getUuid()
//                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
//                        //"Found characteristic : " + characteristic.getUuid()
//                        if (hasProperty(characteristic, BluetoothGattCharacteristic.PROPERTY_READ)) {
//                            // "Read characteristic : " + characteristic.getUuid());
//                            gatt.readCharacteristic(characteristic);
//                            Log.d(TAG, "onServicesDiscovered: cccc"+characteristic);
//                        }
//
//                        if (hasProperty(characteristic, BluetoothGattCharacteristic.PROPERTY_NOTIFY)) {
//                            // "Register notification for characteristic : " + characteristic.getUuid());
//                            gatt.setCharacteristicNotification(characteristic, true);
//                            Log.d(TAG, "onServicesDiscovered:dddd "+characteristic);
//                        }
//                    }
//                }
//            }
//        }
//
//        // 특성 값 읽기 처리
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicRead(gatt, characteristic, status);
//            if (status == BluetoothGatt.GATT_SUCCESS) {
////                if (onReadValueListener == null) return;
//                // This is Background Thread
//                System.out.println(characteristic.getValue().toString());
//                mainThreadHandler.post(
////                        () ->onReadValueListener.onValue(gatt.getDevice(), onReadValueListener.formatter(characteristic))
//                        () -> {
//
//                        }
//                );
//            }
//        }
//
//        // 알림 값 처리
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            super.onCharacteristicChanged(gatt, characteristic);
//            // This is Background Thread
//            System.out.println(characteristic.getValue().toString());
//            mainThreadHandler.post(
////                    ()->onNotifyValueListener.onValue(gatt.getDevice(), onNotifyValueListener.formatter(characteristic))
//                    () -> {
//
//                    }
//            );
//        }
//
//    };

//    // Bluetooth GATT 특성 속성 확인
//    public boolean hasProperty(BluetoothGattCharacteristic characteristic, int property) {
//        int prop = characteristic.getProperties() & property;
//        return prop == property;
//    }

//    @Override
//    public void onValue(BluetoothDevice deivce, XiaomiSensor value) {
//        Log.d(TAG, "onValue: "+String.valueOf(value.temperature));
//    }
//
//    @Override
//    public XiaomiSensor formatter(BluetoothGattCharacteristic characteristic) {
//        Integer value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 0);
//        Integer value2 = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1);
//        float temperature = value * 0.01f;
//        int humidity = (value2 & 0xFF00) >> 8;
//        return new XiaomiSensor(
//                System.currentTimeMillis(),
//                temperature,
//                humidity
//        );
//    }

    public interface OnNotifyValueListener<T> {
        void onValue(BluetoothDevice deivce, T value);

        T formatter(BluetoothGattCharacteristic characteristic);
    }

    public interface OnReadValueListener<T> {
        void onValue(BluetoothDevice deivce, T value);

        T formatter(BluetoothGattCharacteristic characteristic);
    }

    // Bluetooth 알림 값 콜백 설정
    private OnNotifyValueListener onNotifyValueListener = null;

    public MainActivity setOnNotifyValueListener(OnNotifyValueListener onNotifyValueListener) {
        this.onNotifyValueListener = onNotifyValueListener;
        return this;
    }

    // Bluetooth 읽기 값 콜백 설정
    private OnReadValueListener onReadValueListener = null;

    public MainActivity setOnReadValueListener(OnReadValueListener onReadValueListener) {
        this.onReadValueListener = onReadValueListener;
        return this;
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

            @SuppressLint("MissingPermission")
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "기기가 연결 되었습니다", Toast.LENGTH_LONG).show();
                binding.toggleButton.setText("on");
//                Log.d(TAG, "onConnectSuccess: " + BleManager.getInstance().getAllConnectedDevice().toString());
//                BluetoothDevice device = bleDevice.getDevice();
//                device.createBond();
//                pairing();
//                gatt.discoverServices();
//                showData(bleDevice);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                binding.toggleButton.setText("off");

                if (isActiveDisConnected) {//사용자의 의한
                    Toast.makeText(MainActivity.this, "연결이 해제되었습니다", Toast.LENGTH_LONG).show();
                } else {// 의도치 않은
                    Toast.makeText(MainActivity.this, "연결이 해제되었습니다", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @SuppressLint("MissingPermission")
    private void showData(BleDevice bleDevice) {
        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
//            BluetoothGattCharacteristic characteristic;
        for (BluetoothGattService service : gatt.getServices()) {
            Log.d(TAG, "showData: " + service);
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
//                String uuid = service.getUuid().toString();
//                characteristic = service.getCharacteristic(service.getUuid());


//                Log.d(TAG, "showData: "+uuid);
                Log.d(TAG, "showData: " + characteristic);
                Log.d(TAG, "showData: " + characteristic.getUuid().toString());
//                gatt.readCharacteristic(characteristic);

//                Log.d(TAG, "showData: "+HexUtil.formatHexString(service.getCharacteristic(service.getUuid()).getValue(), true));
                BleManager.getInstance().read(
                        bleDevice,
                        characteristic.getService().getUuid().toString(),// 서비스 uuid
                        characteristic.getUuid().toString(), // 캐릭터 uuid
                        new BleReadCallback() {

                            @Override
                            public void onReadSuccess(final byte[] data) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "run: " + HexUtil.formatHexString(data, true));
                                    }
                                });
                            }

                            @Override
                            public void onReadFailure(final BleException exception) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "run: " + exception.toString());
                                    }
                                });
                            }
                        });
            }


//            BleManager.getInstance().read(bleDevice, characteristic.getService().getUuid().toString(),
//                    characteristic.getUuid().toString(), );
        }
    }
}