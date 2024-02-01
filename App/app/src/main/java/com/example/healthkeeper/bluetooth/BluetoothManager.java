package com.example.healthkeeper.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.*;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.*;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import com.example.healthkeeper.bt.BTtestActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class BluetoothManager {

    private static final int REQUEST_ENABLE_BT = 1099; // 블루투스 활성화상태
    private final String TAG = this.getClass().getSimpleName();
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치
    private String deviceName;
    private AppCompatActivity activity;

    public BluetoothManager(AppCompatActivity activity, String deviceName) {
        this.deviceName = deviceName;
        this.activity = activity;
        SetBluetooth();
        // 자동 연결 수행

        startMonitoringBluetoothConnection();
    }

    private Timer timer;

    // 블루투스 연결 상태를 주기적으로 확인하는 메서드
    private void startMonitoringBluetoothConnection() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // BluetoothSocket 객체가 생성되어 있고 연결되어 있다면 isConnected() 메서드가 true를 반환합니다.
                boolean isConnected = isConnected();
                if(!isConnected)autoConnectToDevice();
                // 연결 상태를 로그에 출력합니다.
                Log.d(TAG, "Bluetooth connection status: " + (isConnected ? "Connected" : "Not connected"));
            }
        }, 0, 5000); // 5초마다 상태를 확인합니다. 필요에 따라 간격을 조절할 수 있습니다.
    }

    // 모니터링 중단
    private void stopMonitoringBluetoothConnection() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public boolean isConnected() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }
    @SuppressLint("MissingPermission")
    public void autoConnectToDevice() {
        // 블루투스 장치 검색을 시작합니다.
        bluetoothAdapter.startDiscovery();
        // 블루투스 장치 검색 결과를 수신하기 위한 BroadcastReceiver를 등록합니다.
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // 블루투스 장치를 발견했을 때
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // 발견된 블루투스 장치의 이름이 설정한 디바이스 이름과 일치하는지 확인합니다.
                    if (device != null && device.getName() != null && device.getName().equals(deviceName)) {
                        // 일치하는 디바이스를 찾았으면 연결을 시도합니다.
                        connectDevice();
                        // 블루투스 검색을 중지합니다.
                        bluetoothAdapter.cancelDiscovery();
                    }
                }
            }
        };
        // BroadcastReceiver 등록
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(receiver, filter);
    }

    //  권한 허용 api 사용 -> activity 로 이동
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//            Toast.makeText(BluetoothManager.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
//            Toast.makeText(BluetoothManager.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
    private void checkPermission(){
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.BLUETOOTH_CONNECT)
                .check();


    }

    @SuppressLint("MissingPermission")
    private void SetBluetooth() {
        // 블루투스 활성화하기

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정
        if (bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
            Toast.makeText(activity.getApplicationContext(), "블루투스 미지원 기기입니다.", Toast.LENGTH_LONG).show();
            // 여기에 처리 할 코드를 작성하세요.
        } else { // 디바이스가 블루투스를 지원 할 때

            if (bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)

                isPaired(); // 블루투스 디바이스 선택 함수 호출
            } else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)

                // 블루투스를 활성화 하기 위한 다이얼로그 출력
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // 선택한 값이 onActivityResult 함수에서 콜백된다.

                activity.startActivityForResult(intent, REQUEST_ENABLE_BT);

            }

        }
    }


    /**
     * System Bluetooth Off
     */
    @SuppressLint("MissingPermission")
    public void off() {
        if (bluetoothAdapter.isEnabled())
            bluetoothAdapter.disable();
    }




    @SuppressLint("MissingPermission")
    public void isPaired(){
        devices = bluetoothAdapter.getBondedDevices();
        boolean isPairedDeviceFound = false;
        for (BluetoothDevice device : devices) {
            if (device.getName().equals(deviceName)) { // 여기에 페어링된 기기의 이름을 지정하세요
                isPairedDeviceFound = true;
                bluetoothDevice = device;
                // 페어링된 기기가 발견되었을 때의 처리
                // 여기에서는 해당 기기의 연결 상태를 확인할 수 있습니다.
                int connectionState = device.getBondState();
                if (connectionState == BluetoothDevice.BOND_BONDED) {
                    // 기기가 연결되어 있음을 나타내는 처리
                    // 여기에서는 연결된 상태의 기기와 통신할 수 있습니다.
                } else {
                    // 기기가 페어링되었지만 아직 연결되지 않음을 나타내는 처리
                    connectDevice();
                }
                break;
            }
        }

        if (!isPairedDeviceFound) {
            // 페어링된 기기가 발견되지 않았을 때의 처리
            Toast.makeText(activity.getApplicationContext(), "먼저 Bluetooth 설정에 들어가 페어링 해주세요", Toast.LENGTH_SHORT).show();
        }

    }

//    @SuppressLint("MissingPermission")
//    public void makeList(AppCompatActivity activity ){
//                devices = bluetoothAdapter.getBondedDevices();
//            // 디바이스를 선택하기 위한 다이얼로그 생성
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity.getApplicationContext());
//
//            builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");
//            // 페어링 된 각각의 디바이스의 이름과 주소를 저장
//            List<String> list = new ArrayList<>();
//
//            // 모든 디바이스의 이름을 리스트에 추가
//
//
//            for (BluetoothDevice bluetoothDevice : devices) {
//                list.add(bluetoothDevice.getName());
//            }
//
//            list.add("취소");
//
//            // List를 CharSequence 배열로 변경
//            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
//
//            list.toArray(new CharSequence[list.size()]);
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
//
//                    connectDevice(charSequences[which].toString(), activity);
//
//                }
//
//            });
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
    @SuppressLint("MissingPermission")
    private void connectDevice () {


        Toast.makeText(activity.getApplicationContext(), bluetoothDevice.getName() +"연결 완료", Toast.LENGTH_SHORT).show();
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



}
