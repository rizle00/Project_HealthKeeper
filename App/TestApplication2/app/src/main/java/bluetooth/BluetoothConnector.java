package bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class BluetoothConnector  {
    private final static String TAG = BluetoothConnector.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private String mBluetoothDeviceAddress;
    private int mConnectionState;
    private Context mContext;
    private LocalBroadcastManager mLocalBroadcastManager;

    private GattUpdateListener mGattUpdateListener;
    public void setGattUpdateListener(GattUpdateListener listener) {
        mGattUpdateListener = listener;
    }

    // GATT 연결 상태 변화를 감지하고 리스너에 알림
    private void notifyGattConnectionState(boolean connected) {
        if (mListener != null) {
            if (connected) {
                mListener.onGattConnected();
            } else {
                mListener.onGattDisconnected();
            }
        }
    }

    // 데이터 수신 이벤트를 감지하고 리스너에 알림
    private void notifyDataReceived(int heartRate, double temperature, int accidentStatus) {
        if (mListener != null) {
            mListener.onDataReceived(heartRate, temperature, accidentStatus);
        }
    }

    // GATT 서비스 디스커버리 이벤트를 감지하고 리스너에 알림
    private void notifyGattServicesDiscovered() {
        if (mListener != null) {
            mListener.onGattServicesDiscovered();
        }
    }
    // 생성자에서 컨텍스트 초기화
    public BluetoothConnector(Context context, BluetoothAdapter adapter) {
        this.mContext = context;
        Log.d(TAG, "BluetoothConnector: "+adapter);
        this.mBluetoothAdapter = adapter;
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        Log.d(TAG, "BluetoothConnector: "+mLocalBroadcastManager);
    }
    // 블루투스 연결 시도
    @SuppressLint("MissingPermission")
    public boolean connect(final String address) {
        Log.d(TAG, "connect: "+mBluetoothAdapter);
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = BluetoothAttributes.STATE_CONNECTING;
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
        mBluetoothGatt = device.connectGatt(mContext, true, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = BluetoothAttributes.STATE_CONNECTING;
        return true;
    }

    // 블루투스 연결, 자원 해제
    @SuppressLint("MissingPermission")
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }


    // 블루투스 연결 콜백
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override // 커넥트 상태 업데이트
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = BluetoothAttributes.ACTION_GATT_CONNECTED;
                mConnectionState = BluetoothAttributes.STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

                Log.d(TAG, "onConnectionStateChange: " + gatt.getDevice().getName());

                // 연결됨 상태 변화를 리스너에 알림
                if (mGattUpdateListener != null) {
                    mGattUpdateListener.onGattConnected();
                }


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = BluetoothAttributes.ACTION_GATT_DISCONNECTED;
                mConnectionState = BluetoothAttributes.STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);

                // 연결 해제 상태 변화를 리스너에 알림
                if (mGattUpdateListener != null) {
                    mGattUpdateListener.onGattDisconnected();
                }
            }
        }

        @SuppressLint("MissingPermission")
        @Override // 블루투스 기기에서 넘어오는 서비스 확인
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(BluetoothAttributes.ACTION_GATT_SERVICES_DISCOVERED);
                if (mGattUpdateListener != null) {
                    mGattUpdateListener.onGattServicesDiscovered();
                }
                // 캐릭터 노티파이 설정
                BluetoothGattCharacteristic characteristic =
                        gatt.getService(UUID.fromString(BluetoothAttributes.SERVICE_UUID))
                                .getCharacteristic(UUID.fromString(BluetoothAttributes.CHARACTERISTIC_UUID));
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
                broadcastUpdate(BluetoothAttributes.ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override// 노티파이 데이터 읽을시
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(BluetoothAttributes.ACTION_DATA_AVAILABLE, characteristic);
            if (mGattUpdateListener != null) {
                mGattUpdateListener.onDataAvailable();
            }
        }
    };

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

        if (BluetoothAttributes.CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(BluetoothAttributes.CONFIG_UUID));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    // 액션에 대한 브로드캐스트 업데이트
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        Log.d(TAG, "broadcastUpdate: " + action);
        mLocalBroadcastManager.sendBroadcast(intent);
    }
    // 데이터에 대한 브로드캐스트 업데이트
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
// 데이터 처리 및 업데이트
        Log.d(TAG, "broadcastUpdate: "+mLocalBroadcastManager);
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 1) {
            Log.d(TAG, "broadcastUpdate: "+action);
            Log.d(TAG, "broadcastUpdate: "+new String(data));
            Log.d(TAG, "broadcastUpdate: "+data.length);
            HashMap<String,String> extractedData = extractData(new String(data, StandardCharsets.UTF_8));
            intent.putExtra("heart",extractedData.get("hr") );
            intent.putExtra("temp",extractedData.get("tp") );
            intent.putExtra("accident",extractedData.get("ac") );
        }

        mLocalBroadcastManager.sendBroadcast(intent);
    }
    public HashMap<String, String> extractData(String data) {


        HashMap<String, String> extractedData = new HashMap<>();

        String[] items = data.split(",");
        Log.d(TAG, "extractData: "+items.length);

        for (int i = 0; i < items.length; i += 2) {
            extractedData.put(items[i], items[i + 1]);
        }

        return extractedData;
    }


}
