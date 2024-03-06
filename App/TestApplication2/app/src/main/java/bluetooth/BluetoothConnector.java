package bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class BluetoothConnector {
    private final static String TAG = BluetoothConnector.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private String mBluetoothDeviceAddress;

    public boolean isConnected() {
        return mConnectionState;
    }

    private boolean mConnectionState;
    private Context mContext;
    private BluetoothRepository repository;

    public MutableLiveData<String> heartLiveData = new MutableLiveData<>("0");
    public MutableLiveData<String> accidentLiveData = new MutableLiveData<>("0");
    public MutableLiveData<String> tempLiveData = new MutableLiveData<>("0");
    public MutableLiveData<String> btLiveData = new MutableLiveData<>("on");



    // 생성자에서 컨텍스트 초기화
    public BluetoothConnector(Context context, BluetoothAdapter adapter, BluetoothRepository repository) {
        this.mContext = context;
        this.mBluetoothAdapter = adapter;
        this.repository = repository;
    }

    // 블루투스 연결 시도
    @SuppressLint("MissingPermission")
    public boolean connect(final String address) {
        Log.d(TAG, "connect: " + mBluetoothAdapter);
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            btLiveData.postValue("off");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = true;
                btLiveData.postValue("on");
                return true;
            } else {
                btLiveData.postValue("off");
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            btLiveData.postValue("off");
            return false;
        }
        mBluetoothGatt = device.connectGatt(mContext, true, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = true;
        btLiveData.postValue("on");
        return true;
    }

    // 블루투스 자원 해제
    @SuppressLint("MissingPermission")
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
        btLiveData.postValue("off");
        mBluetoothGatt = null;
    }


    // 블루투스 연결 콜백
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override // 커넥트 상태 업데이트
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnectionState = true;
                btLiveData.postValue("on");
                Log.d(TAG, "onConnectionStateChange: 커넥트 성공");
                mBluetoothGatt.discoverServices();// 서비스 찾기

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                mConnectionState = false;
                btLiveData.postValue("off");
            }
        }

        @SuppressLint("MissingPermission")
        @Override // 블루투스 기기에서 넘어오는 서비스 확인
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {// 서비스 발견 성공시
                // 캐릭터 노티파이 설정
                Log.d(TAG, "onServicesDiscovered: 서비스 발견됨");
                BluetoothGattCharacteristic characteristic =
                        gatt.getService(UUID.fromString(BluetoothAttributes.SERVICE_UUID))
                                .getCharacteristic(UUID.fromString(BluetoothAttributes.CHARACTERISTIC_UUID));
                setCharacteristicNotification(characteristic, true);
            } else {
                //서비스 발견 실패시
                Log.d(TAG, "onServicesDiscovered: " + status);
            }
        }

        @Override // 데이터 읽을 시
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onCharacteristicRead: " + characteristic.getUuid());
            }
        }

        @Override// 노티파이 데이터 읽을시
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            if (characteristic.getValue().length > 1) {// byte가 0 이 되는 경우가 있음
                Log.d(TAG, "onCharacteristicChanged: "+characteristic.getValue().length);
                repository.insertData(handleData(characteristic));
            }

        }
    };

    public HashMap<String, String > handleData(BluetoothGattCharacteristic characteristic) {
        // 데이터 추출 및 세팅
        final byte[] data = characteristic.getValue();
        HashMap<String, String> extractedData = new HashMap<>();
        HashMap<String, String > dataMap = new HashMap<>();
        if (data != null && data.length > 1) {
            String strData = new String(data, StandardCharsets.UTF_8);
            String[] items = strData.split(",");

            for (int i = 0; i < items.length; i += 2) {
                extractedData.put(items[i], items[i + 1]);
            }
            dataMap.put("heart", extractedData.get("hr"));
            dataMap.put("temp", extractedData.get("tp"));
            dataMap.put("accident", extractedData.get("ac"));
            Log.d(TAG, "handleData: " + dataMap.toString());
            setLiveData(dataMap);
        }
        return dataMap;
    }

    private void setLiveData(HashMap<String, String> dataMap) {
        String heart = dataMap.get("heart").toString();
        String temp = dataMap.get("temp").toString();
        String accident = dataMap.get("accident").toString();
        heartLiveData.postValue(heart);
        tempLiveData.postValue(temp);
        accidentLiveData.postValue(accident);
    }



    @SuppressLint("MissingPermission")
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        // 노티파이 설정
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        Log.d(TAG, "setCharacteristicNotification: " + characteristic);

        if (BluetoothAttributes.CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(BluetoothAttributes.CONFIG_UUID));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }


}
