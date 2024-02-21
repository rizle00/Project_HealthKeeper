package com.example.testapplication2;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.*;
import android.content.*;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MyService extends Service {
    private final static String TAG = MyService.class.getSimpleName();
    //블루투스 UUID들
    public final static String SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public final static String CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public final static String CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    public final static String DEVICE_UUID = "0000180A-0000-1000-8000-00805f9b34fb";

    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    private BluetoothManager mBluetoothManager;
    private List<BleDevice> deviceList;


    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private boolean mConnected = false;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    @SuppressLint("MissingPermission")
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    // ble 초기화
    public boolean initialize(Context context) {
        BleManager.getInstance().init(getApplication());
        mBluetoothManager = BleManager.getInstance().getBluetoothManager();
        // 매니저 체크
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5 * 1000)
                .setConnectOverTime(20 * 1000)
                .setOperateTimeout(5 * 1000);

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        } else startScan(context);

        return true;
    }

    // 스캔 룰 지정
    private void setScanRule(String name) {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setDeviceName(true, name)   // 지정된 브로드캐스트 이름의 장치만 스캔합니다. 선택 사항입니다. 배열가능
//                .setDeviceMac(mac)                  // 지정된 MAC 주소의 장치만 스캔합니다. 선택 사항입니다.50:65:83:09:4D:66
                .setAutoConnect(true)      // 연결 시 autoConnect 매개변수입니다. 선택 사항입니다. 기본값은 false입니다.
                .setScanTimeOut(10 * 1000)              // 스캔 타임아웃 시간입니다. 선택 사항입니다. 기본값은 10초입니다.
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    // 스캔 시작
    private void startScan(Context context) {
        // 기기이름 배열로 추가 가능, 맥주소 등  세팅가능
        setScanRule("HM10");
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Toast.makeText(getApplicationContext(), "스캔을 시작합니다", Toast.LENGTH_SHORT).show();
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
                showDeviceList(scanResultList, context);
            }
        });
    }

    private void showDeviceList(List<BleDevice> scanResultList, Context context) {
        if (scanResultList.size() == 0) { // 스캔된 장치가 없는 경우.
            Toast.makeText(getApplicationContext(), "근처에 연결 가능한 장치가 없습니다. 재검색을 시도합니다.", Toast.LENGTH_LONG).show();

            startScan(context);
        } else {// 스캔된 장치가 있는 경우.

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                        Toast.makeText(context, "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
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
//                            final Intent intent = new Intent(context, TestActivity.class);
//                            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
//                            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//                            startActivity(intent);
//                            Log.d(TAG, "onClick: "+intent);
                            connect(device.getAddress());
                        }

                    }
                }

            });

            builder.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

    @SuppressLint("MissingPermission")
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, true, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    @SuppressLint("MissingPermission")
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

                mBluetoothGatt.discoverServices();
                Log.d(TAG, "onConnectionStateChange: " + gatt.getDevice().getName());
//                gatt.getDevice().createBond();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

                BluetoothGattCharacteristic ch = gatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
                setCharacteristicNotification(ch, true);
                Log.d(TAG, "onServicesDiscovered: ");
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            //service uuid, char uuid
            BluetoothGattCharacteristic ch = gatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")).getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
            HashMap<String, String> extractedData = extractData(ch);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        Log.d(TAG, "broadcastUpdate: " + action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BtService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BtService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BtService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BtService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter(), RECEIVER_EXPORTED);
//// 리시버 제대로 등록 ... 필수 암시적은 잘 등록 안되기도
//        if (mBluetoothLeService != null) {
//            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
//            Log.d(TAG, "Connect request result=" + result);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mGattUpdateReceiver);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unbindService(mServiceConnection);
//        mBluetoothLeService = null;
//    }
//    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            Log.d(TAG, "onReceive: "+action);
//            if (BtService.ACTION_GATT_CONNECTED.equals(action)) {
//                mConnected = true;
//                Log.d(TAG, "onReceive: "+action);
//            } else if (BtService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                mConnected = false;
//            } else if (BtService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//                // Show all the supported services and characteristics on the user interface.
//                Log.d(TAG, "onReceive: "+action);
//            } else if (BtService.ACTION_DATA_AVAILABLE.equals(action)) {
//                Log.d(TAG, "onReceive: "+action);
//                sendData(intent.getStringExtra(BtService.EXTRA_DATA));
//            }
//        }
//    };

    @SuppressLint("MissingPermission")
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }
    @SuppressLint("MissingPermission")
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        Log.d(TAG, "setCharacteristicNotification: " + characteristic);

        // This is specific to Heart Rate Measurement.
        if (CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public static HashMap<String, String> extractData(BluetoothGattCharacteristic characteristic) {

        String data = new String(characteristic.getValue());

        HashMap<String, String> extractedData = new HashMap<>();

        String[] items = data.split(",");

        for (int i = 0; i < items.length; i += 2) {
            extractedData.put(items[i], items[i + 1]);
        }

        return extractedData;
    }
    private void sendData(String data) {
        if (data != null) {
        }
    }


}