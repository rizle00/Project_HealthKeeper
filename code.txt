package com.example.my55_bluetoothautopairing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 3;
    public BluetoothAdapter mBluetoothAdapter = null;
    Set<BluetoothDevice> bluetooth_device;
    int mPairedDeviceCount;
    BluetoothDevice mRemoteDevice;
    BluetoothSocket mSocket;
    InputStream mInputStream;
    OutputStream mOutputStream;
    Thread mWorkerThread;
    int readBufferPositon;      //버퍼 내 수신 문자 저장 위치
    byte[] readBuffer;      //수신 버퍼
    byte mDelimiter = 10;

    IntentFilter stateFilter;
    // 연결되었는가
    int isConnect = 0;
    int isContinue = 0;
    int connCount = 0;
    // 일정시간마다 실행
    CountDownTimer CDT;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDangerousPermissions();

        textView = findViewById(R.id.textView);
        bluetooth_device = new HashSet<>();

        // 리시버 등록
        regiserRec();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();   //블루투스 adapter 획득
        boolean isStart = mBluetoothAdapter.startDiscovery(); //블루투스 기기 검색 시작
        Log.d("main", "onCreate: isStart " + isStart);

        CDT = new CountDownTimer(60 * 1000, 15000) {
            public void onTick(long millisUntilFinished) {
                textView.append("시간안에 들어옴... : isConnect"+ isConnect + " => isContinue : " + isContinue + "\n");
                connCount++;
                if(connCount >= 3 && isContinue == 1){
                    isContinue = 0;
                    connCount = 0;
                }
                //반복실행할 구문
                if(isConnect == 0 && isContinue == 0){
                    mBluetoothAdapter.startDiscovery(); //블루투스 기기 검색 시작
                    isContinue = 1;
                    textView.append("블루투스 연결을 시도합니다...\n");
                }

            }
            public void onFinish() {
                //마지막에 실행할 구문
                CDT.start();
            }
        }.start();

        //CDT.start(); //CountDownTimer 실행
        //CDT.cancel();// 타이머 종료
    }

    public void regiserRec(){
        stateFilter = new IntentFilter();
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //BluetoothAdapter.ACTION_STATE_CHANGED : 블루투스 상태변화 액션
        stateFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        stateFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED); //연결 확인
        stateFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED); //연결 끊김 확인
        stateFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        stateFilter.addAction(BluetoothDevice.ACTION_FOUND);    //기기 검색됨
        stateFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);   //기기 검색 시작
        stateFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);  //기기 검색 종료
        stateFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(mBluetoothStateReceiver, stateFilter);
    }

    BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();   //입력된 action
            //Toast.makeText(context, "받은 액션 : "+action , Toast.LENGTH_SHORT).show();
            Log.d("Bluetooth action", action);
            textView.append("받은 액션 : " + action + "\n" );
            final BluetoothDevice device =   intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = null;
            if (device != null) {
                name = device.getName();    //broadcast를 보낸 기기의 이름을 가져온다.
            }
            //입력된 action에 따라서 함수를 처리한다
            switch (action){
                case BluetoothAdapter.ACTION_STATE_CHANGED: //블루투스의 연결 상태 변경
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    switch(state) {
                        case BluetoothAdapter.STATE_OFF:

                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:

                            break;
                        case BluetoothAdapter.STATE_ON:

                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:

                            break;
                    }

                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:  //블루투스 기기 연결
                    isConnect = 1;
                    Log.d("Bluetooth", "ACTION_ACL_CONNECTED");
                    textView.append("받은 액션 : " + "ACTION_ACL_CONNECTED" + "\n" );
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:

                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:   //블루투스 기기 끊어짐
                    isConnect = 0;
                    Log.d("Bluetooth", "ACTION_ACL_DISCONNECTED");
                    textView.append("받은 액션 : " + "ACTION_ACL_DISCONNECTED" + "\n" );
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_STARTED: //블루투스 기기 검색 시작

                    break;
                case BluetoothDevice.ACTION_FOUND:  //블루투스 기기 검색 됨, 블루투스 기기가 근처에서 검색될 때마다 수행됨
                    String device_name = device.getName();
                    textView.append("받은 액션 : " + "ACTION_FOUND : " + device_name + "\n" );
                    String device_Address = device.getAddress();
                    textView.append("받은 액션 : " + "ACTION_FOUND : " + device_Address + "\n" );

                    //Log.d("Bluetooth Name: ", device_name);
                    //Log.d("Bluetooth Mac Address: ", device_Address);

                    //본 함수는 블루투스 기기 이름의 앞글자가 "han"으로 시작하는 기기만을 검색하는 코드이다
                    if(device_name != null && device_name.length() > 5){
                        Log.d("Bluetooth Name: ", device_name);
                        Log.d("Bluetooth Mac Address: ", device_Address);
                        if(device_name.substring(0,5).equals("hanul")){
                            bluetooth_device.add(device);
                        }
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:    //블루투스 기기 검색 종료
                    Log.d("Bluetooth", "Call Discovery finished");
                    textView.append("받은 액션 : " + "ACTION_DISCOVERY_FINISHED:: " + "\n" );

                    isContinue = 0;
                    StartBluetoothDeviceConnection();   //원하는 기기에 연결
                    break;
                case BluetoothDevice.ACTION_PAIRING_REQUEST:

                    break;
            }

        }
    };

    public void StartBluetoothDeviceConnection() {
        //Bluetooth connection start
        if (bluetooth_device.size() == 0) {
            Toast.makeText(this, "There is no device", Toast.LENGTH_SHORT).show();
        }

        // 기기 이름을 선택한 경우 선택한 기기 이름과 같은 블루투스 객체를 찾아서 연결을 시도한다
        for (BluetoothDevice bt_device : bluetooth_device) {
            if (bt_device.getName().contains("hanul")) {
                Log.d("Bluetooth Connect", bt_device.getName());
                connectToSelectedDevice(bt_device);  //해당하는 블루투스 객체를 이용하여 연결 시도
                Log.d("Bluetooth Connect", "ConnectBluetoothDevice");
                break;
            }
        }


    }
    void connectToSelectedDevice(BluetoothDevice selectedDevice) {
        // BluetoothDevice 원격 블루투스 기기를 나타냄.
        mRemoteDevice = selectedDevice;
        // java.util.UUID.fromString : 자바에서 중복되지 않는 Unique 키 생성.
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 소켓 생성, RFCOMM 채널을 통한 연결.
            // createRfcommSocketToServiceRecord(uuid) : 이 함수를 사용하여 원격 블루투스 장치와
            //                                           통신할 수 있는 소켓을 생성함.
            // 이 메소드가 성공하면 스마트폰과 페어링 된 디바이스간 통신 채널에 대응하는
            //  BluetoothSocket 오브젝트를 리턴함.
            mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);
            mSocket.connect(); // 소켓이 생성 되면 connect() 함수를 호출함으로써 두기기의 연결은 완료된다.

            // 데이터 송수신을 위한 스트림 얻기.
            // BluetoothSocket 오브젝트는 두개의 Stream을 제공한다.
            // 1. 데이터를 보내기 위한 OutputStrem
            // 2. 데이터를 받기 위한 InputStream
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();

            // 데이터 수신 준비.
            //beginListenForData();

        } catch (Exception e) { // 블루투스 연결 중 오류 발생
            Toast.makeText(getApplicationContext(),
                    "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//            finish();  // App 종료
        }
    }


    /*public void StartBluetoothDeviceConnection(){
        //Bluetooth connection start
        if(bluetooth_device.size() == 0){
            Toast.makeText(this,"There is no device", Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 목록");

        // 페어링 된 블루투스 장치의 이름 목록 작성
        List<String> listItems = new ArrayList<String>();
        for (BluetoothDevice bt_device : bluetooth_device) {
            listItems.add(bt_device.getName());
        }
        //listItems.add("Cancel");    // 취소 항목 추가

        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog dialog_ = (Dialog) dialog;

                if (which == bluetooth_device.size()) {
                    // 연결할 장치를 선택하지 않고 '취소'를 누른 경우

                } else {
                    // 기기 이름을 선택한 경우 선택한 기기 이름과 같은 블루투스 객체를 찾아서 연결을 시도한다
                    for (BluetoothDevice bt_device : bluetooth_device) {
                        if (bt_device.getName().equals(items[which].toString())) {
                            Log.d("Bluetooth Connect", bt_device.getName());
                            connectToSelectedDevice(bt_device.getName());  //해당하는 블루투스 객체를 이용하여 연결 시도
                            Log.d("Bluetooth Connect", "ConnectBluetoothDevice");
                            break;
                        }
                    }
                }

            }
        });

        builder.setCancelable(false);    // 뒤로 가기 버튼 사용 금지
        AlertDialog alert = builder.create();
        alert.show();
        Log.d("Bluetooth Connect", "alert start");
    }*/

    /*void connectToSelectedDevice(String selectedDeviceName) {
        // BluetoothDevice 원격 블루투스 기기를 나타냄.
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
        // java.util.UUID.fromString : 자바에서 중복되지 않는 Unique 키 생성.
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 소켓 생성, RFCOMM 채널을 통한 연결.
            // createRfcommSocketToServiceRecord(uuid) : 이 함수를 사용하여 원격 블루투스 장치와
            //                                           통신할 수 있는 소켓을 생성함.
            // 이 메소드가 성공하면 스마트폰과 페어링 된 디바이스간 통신 채널에 대응하는
            //  BluetoothSocket 오브젝트를 리턴함.
            mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);
            mSocket.connect(); // 소켓이 생성 되면 connect() 함수를 호출함으로써 두기기의 연결은 완료된다.

            // 데이터 송수신을 위한 스트림 얻기.
            // BluetoothSocket 오브젝트는 두개의 Stream을 제공한다.
            // 1. 데이터를 보내기 위한 OutputStrem
            // 2. 데이터를 받기 위한 InputStream
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();

            // 데이터 수신 준비.
            //beginListenForData();

        } catch (Exception e) { // 블루투스 연결 중 오류 발생
            Toast.makeText(getApplicationContext(),
                    "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//            finish();  // App 종료
        }
    }*/

    // 블루투스 장치의 이름이 주어졌을때 해당 블루투스 장치 객체를 페어링 된 장치 목록에서 찾아내는 코드.
    /*BluetoothDevice getDeviceFromBondedList(String name) {
        // BluetoothDevice : 페어링 된 기기 목록을 얻어옴.
        BluetoothDevice selectedDevice = null;
        // getBondedDevices 함수가 반환하는 페어링 된 기기 목록은 Set 형식이며,
        // Set 형식에서는 n 번째 원소를 얻어오는 방법이 없으므로 주어진 이름과 비교해서 찾는다.
        for (BluetoothDevice deivce : bluetooth_device) {
            // getName() : 단말기의 Bluetooth Adapter 이름을 반환
            if (name.equals(deivce.getName())) {
                selectedDevice = deivce;
                break;
            }
        }
        return selectedDevice;
    }*/

    /*public void ConnectBluetoothDevice(final BluetoothDevice device){

        bluetooth_device = mBluetoothAdapter.getBondedDevices();
        mPairedDeviceCount = bluetooth_device.size();

        //pairing되어 있는 기기의 목록을 가져와서 연결하고자 하는 기기가 이전 기기 목록에 있는지 확인
        boolean already_bonded_flag = false;
        if(mPairedDeviceCount > 0){
            for (BluetoothDevice bonded_device : bluetooth_device) {
                if(device.getName().equals(bonded_device.getName())){
                    already_bonded_flag = true;
                }
            }
        }
        //pairing process
        //만약 pairing기록이 있으면 바로 연결을 수행하며, 없으면 createBond()함수를 통해서 pairing을 수행한다.
        if(!already_bonded_flag){
            try {
                //pairing수행
                device.createBond();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            ConnectBluetoothDevice(device); //connectToSelectedDevice(device);
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        //regiserRec();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 리시버 해제
        //unregisterReceiver(mBluetoothStateReceiver);
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}