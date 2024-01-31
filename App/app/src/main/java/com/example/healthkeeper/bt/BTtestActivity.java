package com.example.healthkeeper.bt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityBttestBinding;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.main.MainActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BTtestActivity extends AppCompatActivity {
    private static final int BLUETOOTH_SETTINGS_REQUEST_CODE = 1001;
    private final String TAG = this.getClass().getSimpleName();

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화상태
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치
    int pariedDeviceCount;

    IntentFilter stateFilter;
    int isConnect = 0;
    int isContinue = 0;
    int connCount = 0;
    CountDownTimer CDT;
    ActivityBttestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBttestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPermission();
        SetBluetooth();

    }

    //    BluetoothGatt
    @SuppressLint("MissingPermission")
    private void SetBluetooth() {
        // 블루투스 활성화하기

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정
        if (bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
            Toast.makeText(getApplicationContext(), "블루투스 미지원 기기입니다.", Toast.LENGTH_LONG).show();
            // 여기에 처리 할 코드를 작성하세요.
        } else { // 디바이스가 블루투스를 지원 할 때

            if (bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
//                scanDevice();
                selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
            } else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)

                // 블루투스를 활성화 하기 위한 다이얼로그 출력
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // 선택한 값이 onActivityResult 함수에서 콜백된다.


                startActivityForResult(intent, REQUEST_ENABLE_BT);

            }

        }
    }

    public void bluetoothPairing(BluetoothAdapter bluetoothAdapter,Set<BluetoothDevice> pairedDevices ){
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        boolean isPairedDeviceFound = false;
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().equals("YourDeviceName")) { // 여기에 페어링된 기기의 이름을 지정하세요
                isPairedDeviceFound = true;
                // 페어링된 기기가 발견되었을 때의 처리
                // 여기에서는 해당 기기의 연결 상태를 확인할 수 있습니다.
                int connectionState = device.getBondState();
                if (connectionState == BluetoothDevice.BOND_BONDED) {
                    // 기기가 연결되어 있음을 나타내는 처리
                    // 여기에서는 연결된 상태의 기기와 통신할 수 있습니다.
                } else {
                    // 기기가 페어링되었지만 아직 연결되지 않음을 나타내는 처리
                }
                break;
            }
        }

        if (!isPairedDeviceFound) {
            // 페어링된 기기가 발견되지 않았을 때의 처리
        }

    }
    public void bluetoothSetting(){
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
//        intent.setData(Uri.parse("package:"+getApplicationContext().getPackageName()));
        startActivityForResult(intent, BLUETOOTH_SETTINGS_REQUEST_CODE);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_SETTINGS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Bluetooth 설정이 성공적으로 완료됨
                // 여기에서 메인화면으로 돌아가는 등의 작업을 수행할 수 있습니다.
            } else {
                // Bluetooth 설정이 취소됨 또는 실패함
                // 처리할 작업을 수행합니다.
            }
        }
    }


//    boolean foundDevice = false;
//
//// ...
//
//    private void scanDevice() {
//        // Progress State Text
////        progressState.postValue("device 스캔 중...");
//
//        // 리시버 등록
//        registerBluetoothReceiver();
//
//        // 블루투스 기기 검색 시작
//
//        foundDevice = false;
//        if (bluetoothAdapter != null) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//
//                return;
//            }
//            bluetoothAdapter.startDiscovery();
//        }
//    }
//    private BroadcastReceiver mBluetoothStateReceiver = null;
//
//// ...
//
//   private void registerBluetoothReceiver() {
//        // intentfilter
//        IntentFilter stateFilter = new IntentFilter();
//        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); // BluetoothAdapter.ACTION_STATE_CHANGED : 블루투스 상태변화 액션
//        stateFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
//        stateFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED); // 연결 확인
//        stateFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED); // 연결 끊김 확인
//        stateFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        stateFilter.addAction(BluetoothDevice.ACTION_FOUND); // 기기 검색됨
//        stateFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); // 기기 검색 시작
//        stateFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); // 기기 검색 종료
//        stateFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
//        mBluetoothStateReceiver = new BroadcastReceiver() {
//            @SuppressLint("MissingPermission")
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction(); // 입력된 action
//                if (action != null) {
//                    Log.d("Bluetooth action", action);
//                }
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                String name = null;
//                if (device != null) {
//                    name = device.getName(); // broadcast를 보낸 기기의 이름을 가져온다.
//                }
//                switch (action) {
//                    case BluetoothAdapter.ACTION_STATE_CHANGED:
//                        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
//                        switch (state) {
//                            case BluetoothAdapter.STATE_OFF:
//                                break;
//                            case BluetoothAdapter.STATE_TURNING_OFF:
//                                break;
//                            case BluetoothAdapter.STATE_ON:
//                                break;
//                            case BluetoothAdapter.STATE_TURNING_ON:
//                                break;
//                        }
//                        break;
//                    case BluetoothDevice.ACTION_ACL_CONNECTED:
//                        break;
//                    case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
//                        break;
//                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
//                        // 디바이스가 연결 해제될 경우
//                        connected.postValue(false);
//                        break;
//                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
//                        break;
//                    case BluetoothDevice.ACTION_FOUND:
//                        if (!foundDevice) {
//                            String device_name = device.getName();
//                            String device_Address = device.getAddress();
//                            // 블루투스 기기 이름의 앞글자가 "RNM"으로 시작하는 기기만을 검색한다
//                            if (device_name != null && device_name.length() > 4) {
//                                if (device_name.substring(0, 3).equals("RNM")) {
//                                    targetDevice = device;
//                                    foundDevice = true;
//                                    // 찾은 디바이스에 연결한다.
//                                    connectToTargetedDevice(targetDevice);
//                                }
//                            }
//                        }
//                        break;
//                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
//                        if (!foundDevice) {
//                            // Toast message
//                            Util.showNotification("디바이스를 찾을 수 없습니다. 다시 시도해 주세요.");
//                            // Progress 해제
//                            inProgress.postValue(Event(false));
//                        }
//                        break;
//                }
//            }
//        };
//
//        // 리시버 등록
//        MyApplication.getApplicationContext().registerReceiver(
//                mBluetoothStateReceiver,
//                stateFilter
//        );
//    }




    @SuppressLint("MissingPermission")
    public void selectBluetoothDevice() {

        // 이미 페어링 되어있는 블루투스 기기를 찾습니다.
        devices = bluetoothAdapter.getBondedDevices();
        String deviceName = "TESTA";

        // 페어링 된 디바이스의 크기를 저장
        pariedDeviceCount = devices.size();
        boolean deviceFound = false;
        List<String> list = new ArrayList<>();

        for (BluetoothDevice bluetoothDevice : devices) {
            list.add(bluetoothDevice.getName());
            if (bluetoothDevice.getName() != null && bluetoothDevice.getName().equals(deviceName)) {
                deviceFound = true;
                connectDevice(bluetoothDevice);
                break;
            }
        }

        // 페어링 되어있는 장치가 없는 경우
        if(pariedDeviceCount == 0 || !deviceFound) {
            // 페어링을 하기위한 함수 호출
            Toast.makeText(getApplicationContext(), "먼저 Bluetooth 설정에 들어가 페어링 해주세요", Toast.LENGTH_SHORT).show();
            bluetoothSetting();

        }

//        // 페어링 되어있는 장치가 있는 경우
//        else {
//            // 디바이스를 선택하기 위한 다이얼로그 생성
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");
//            // 페어링 된 각각의 디바이스의 이름과 주소를 저장
//            List<String> list = new ArrayList<>();
//
//            // 모든 디바이스의 이름을 리스트에 추가
//            boolean deviceFound = false;
//
//            for (BluetoothDevice bluetoothDevice : devices) {
//                list.add(bluetoothDevice.getName());
//                if (bluetoothDevice.getName() != null && bluetoothDevice.getName().equals(deviceName)) {
//                    deviceFound = true;
//                    break;
//                }
//            }
//
//            if (!deviceFound) {
//                Toast.makeText(getApplicationContext(), "먼저 Bluetooth 설정에 들어가 페어링 해주세요", Toast.LENGTH_SHORT).show();
//                bluetoothSetting();
//
//            }
//
//
//            list.add("취소");
//
//
//
//            // List를 CharSequence 배열로 변경
//            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
//
//            list.toArray(new CharSequence[list.size()]);
//
//
//
//            // 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너
//
//            builder.setItems(charSequences, new DialogInterface.OnClickListener() {
//
//                @Override
//
//                public void onClick(DialogInterface dialog, int which) {
//
//                    // 해당 디바이스와 연결하는 함수 호출
//                    connectDevice(charSequences[which].toString());
//
//                }
//
//            });
//
//
//
//            // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정
//
//            builder.setCancelable(false);
//
//            // 다이얼로그 생성
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//
//        }

    }

    @SuppressLint("MissingPermission")
    public void connectDevice(BluetoothDevice bluetoothDevice) {
        // 페어링 된 디바이스들을 모두 탐색
//        for(BluetoothDevice tempDevice : devices) {
//            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
//            if(deviceName.equals(tempDevice.getName())) {
//                bluetoothDevice = tempDevice;
//                break;
//            }
//        }
        Toast.makeText(this, bluetoothDevice +"연결 완료", Toast.LENGTH_SHORT).show();
        // UUID 생성

        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            // 데이터 수신 함수 호출
            receiveData();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void receiveData() {

        final Handler handler = new Handler();
        // 데이터를 수신하기 위한 버퍼를 생성
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        // 데이터를 수신하기 위한 쓰레드 생성
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {

                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }

                    try {
                        // 데이터를 수신했는지 확인합니다.
                        int byteAvailable = inputStream.available();
                        // 데이터가 수신 된 경우
                        if(byteAvailable > 0) {
                            // 입력 스트림에서 바이트 단위로 읽어 옵니다.

                            byte[] bytes = new byte[byteAvailable];

                            inputStream.read(bytes);

                            // 입력 스트림 바이트를 한 바이트씩 읽어 옵니다.
                            for(int i = 0; i < byteAvailable; i++) {
                                byte tempByte = bytes[i];
                                // 개행문자를 기준으로 받음(한줄)
                                if(tempByte == '\n') {
                                    // readBuffer 배열을 encodedBytes로 복사
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    // 인코딩 된 바이트 배열을 문자열로 변환
                                    final String text = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            // 아두이노에서 받은 출력값
                                            Log.d(TAG, "run: text ="+ text);

                                        }
                                    });
                                } // 개행 문자가 아닐 경우
                                else {
                                    readBuffer[readBufferPosition++] = tempByte;
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        // 1초마다 받아옴
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        workerThread.start();
    }

    //  권한 허용 api 사용
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(BTtestActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(BTtestActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
    private void checkPermission(){
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.BLUETOOTH_CONNECT)
                .check();


    }




}