package com.example.testapplication2;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class MyService extends Service {

        private final static String TAG = MyService.class.getSimpleName();

        private Thread mThread;

        // Bluetooth UUID 상수
        public final static String SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
        public final static String CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
        public final static String CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb";
        public final static String DEVICE_UUID = "0000180A-0000-1000-8000-00805f9b34fb";

        // Bluetooth 연결 상태 상수
        private static final int STATE_DISCONNECTED = 0;
        private static final int STATE_CONNECTING = 1;
        private static final int STATE_CONNECTED = 2;
        private int mConnectionState = STATE_DISCONNECTED;

        // Bluetooth 관련 멤버 변수
        private final static String deviceName = "HM10";
        private BluetoothManager mBluetoothManager;
        private BluetoothAdapter mBluetoothAdapter;
        private String mBluetoothDeviceAddress;
        private BluetoothGatt mBluetoothGatt;
        private BluetoothGattCharacteristic characteristic;

        // 블루투스 디바이스 리스트
        private Set<BluetoothDevice> deviceList;
//    private List<BleDevice> scanedList;




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

        // Service 바인더 ============================
        public class LocalBinder extends Binder {
            MyService getService() {
                return MyService.this;
            }
        }

        private final IBinder mBinder = new LocalBinder();

        @Override
        public IBinder onBind(Intent intent) {
            return mBinder;
        }
        //  서비스 연결 해제 시
        @Override
        public boolean onUnbind(Intent intent) {
            close();
            return super.onUnbind(intent);
        }
        //    ble 연결 해제
        @SuppressLint("MissingPermission")
        public void close() {
            if (mBluetoothGatt == null) {
                return;
            }
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
        //===========================
        // ble 초기화
        @SuppressLint("MissingPermission")
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
            deviceList = mBluetoothAdapter.getBondedDevices();
            if (mBluetoothAdapter == null) {
                Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
                return false;
            } else if(!checkPairing()) startScan(context);

            return true;
        }
        // 디바이스 있는지 확인
        @SuppressLint("MissingPermission")
        private boolean checkPairing() {

            for (BluetoothDevice device : deviceList) {
                if (device.getName() != null && device.getName().equals(deviceName)) {
                    connect(device.getAddress());
                    return true;
                }
            }
            return false;
        }

        // 스캔 룰 지정, 기기이름(배열가능), 맥주소, 등..
        private void setScanRule() {
            BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                    .setDeviceName(true, deviceName)
                    .setAutoConnect(true)
                    .setScanTimeOut(10 * 1000)
                    .build();
            BleManager.getInstance().initScanRule(scanRuleConfig);
        }

        // 스캔 시작
        private void startScan(Context context) {
            // 기기이름 배열로 추가 가능, 맥주소 등  세팅가능
            setScanRule();
            BleManager.getInstance().scan(new BleScanCallback() {
                @Override
                public void onScanStarted(boolean success) {
                    Toast.makeText(getApplicationContext(), "스캔을 시작합니다", Toast.LENGTH_SHORT).show();
                }

//            @Override
//            public void onLeScan(BleDevice bleDevice) {
//                super.onLeScan(bleDevice);
//            }

                @Override
                public void onScanning(BleDevice bleDevice) {

                    if (bleDevice.getName().equals(deviceName)) {
                        BleManager.getInstance().cancelScan();
                    }
                }

                @Override
                public void onScanFinished(List<BleDevice> scanResultList) {
//                scanedList = scanResultList;
                    showDeviceList(scanResultList, context);
                }
            });
        }
        // 스캔 결과 보여주기
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
//                scanedList.add(device);
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
                            //  선택한 기기를 가져옴
                            for (BleDevice device : scanResultList) {
                                if (device.getName().equals(items[item])) {
                                    selectedDevice = device;
                                    break;
                                }
                            }
                            if (selectedDevice != null) {
                                BluetoothDevice device = selectedDevice.getDevice();
                                Log.d(TAG, "onClick: "+device.getName());
                                if(device.createBond())
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
        // 블루투스 연결 시도
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
        // 블루투스 연결 해제
        @SuppressLint("MissingPermission")
        public void disconnect() {
            if (mBluetoothAdapter == null || mBluetoothGatt == null) {
                Log.w(TAG, "BluetoothAdapter not initialized");
                return;
            }
            mBluetoothGatt.disconnect();
        }
        // 블루투스 연결 콜백
        private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
            @SuppressLint("MissingPermission")
            @Override // 커넥트 상태 업데이트
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

                    Log.d(TAG, "onConnectionStateChange: " + gatt.getDevice().getName());


                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    intentAction = ACTION_GATT_DISCONNECTED;
                    mConnectionState = STATE_DISCONNECTED;
                    Log.i(TAG, "Disconnected from GATT server.");
                    broadcastUpdate(intentAction);
                }
            }

            @SuppressLint("MissingPermission")
            @Override // 블루투스 기기에서 넘어오는 서비스 확인
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                    // 캐릭터 노티파이 설정
                    characteristic = gatt.getService(UUID.fromString(SERVICE_UUID)).getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID));
                    setCharacteristicNotification(characteristic, true);
                    Log.d(TAG, "onServicesDiscovered: ");
                } else {
                    Log.w(TAG, "onServicesDiscovered received: " + status);
                }
            }

            @Override // 데이터 읽을 시
            public void onCharacteristicRead(BluetoothGatt gatt,
                                             BluetoothGattCharacteristic characteristic,
                                             int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                }
            }

            @Override// 노티파이 데이터 읽을시
            public void onCharacteristicChanged(BluetoothGatt gatt,
                                                BluetoothGattCharacteristic characteristic) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        };
        // 액션에 대한 브로드캐스트 업데이트
        private void broadcastUpdate(final String action) {
            final Intent intent = new Intent(action);
            Log.d(TAG, "broadcastUpdate: " + action);
            sendBroadcast(intent);
        }
        // 데이터에 대한 브로드캐스트 업데이트
        private void broadcastUpdate(final String action,
                                     final BluetoothGattCharacteristic characteristic) {
            final Intent intent = new Intent(action);
// 데이터 처리 및 업데이트
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                HashMap<String,String> extractedData = extractData(new String(data, StandardCharsets.UTF_8));
                intent.putExtra("heart",extractedData.get("hr") );
                intent.putExtra("temp",extractedData.get("tp") );
                intent.putExtra("accident",extractedData.get("ac") );
            }

            sendBroadcast(intent);
        }




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

            if (CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                        UUID.fromString(CONFIG_UUID));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            }
        }

        public static HashMap<String, String> extractData(String data) {


            HashMap<String, String> extractedData = new HashMap<>();

            String[] items = data.split(",");

            for (int i = 0; i < items.length; i += 2) {
                extractedData.put(items[i], items[i + 1]);
            }

            return extractedData;
        }

    }