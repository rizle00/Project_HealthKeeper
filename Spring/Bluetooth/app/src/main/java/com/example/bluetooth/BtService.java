package com.example.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.*;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BtService extends Service {

    private final IBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        BtService getService() {
            return BtService.this;
        }
    }


    public static final int INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701;

    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private final List<BluetoothGatt> gattList = new ArrayList<>();
    private final HashMap<String, BluetoothDevice> hashDeviceMap = new HashMap<>();
    private final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    private boolean scanning = false;


    /**
     * 시스템 블루투스 기능 활성 체크
     */
    public boolean isOn()
    {
        return adapter.isEnabled();
    }

    /**
     * System Bluetooth On
     */
    @SuppressLint("MissingPermission")
    public void on(AppCompatActivity activity) {
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, INTENT_REQUEST_BLUETOOTH_ENABLE);
        }
    }

    /**
     * System Bluetooth On Result
     */
    public boolean onActivityResult(int requestCode, int resultCode)
    {
        return requestCode == BtService.INTENT_REQUEST_BLUETOOTH_ENABLE
                && Activity.RESULT_OK == resultCode;
    }


    /**
     * System Bluetooth Off
     */
    @SuppressLint("MissingPermission")
    public void off() {
        if (adapter.isEnabled())
            adapter.disable();
    }



    /**
     * Start Scan
     */
    @SuppressLint("MissingPermission")
    public void scanDevices() { // activity 에서 호출
        if (!adapter.isEnabled()) return;// 어댑터 활성 체크
        if (scanning) return; // 스캐닝 중 .. 리턴

        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();//  스캐너 등록

        mainThreadHandler.postDelayed(() -> {
            scanning = false; // 2분 후 스캔 중지
            scanner.stopScan(callback);
        }, 2 * 60 * 1000);

        scanning = true;// 스캐닝 시작
        scanner.startScan(callback);
    }

    private final ScanCallback callback = new ScanCallback() {//  스캔후 기기 추가
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            Map<ParcelUuid, byte[]> serviceDataMap = result.getScanRecord().getServiceData();
            if( serviceDataMap == null ) return;
            if( onCheckModelListener == null ) return;
            for( ParcelUuid parcelUuid : serviceDataMap.keySet() )
            {
                if( onCheckModelListener.isChecked(result.getScanRecord().getServiceData(parcelUuid)))
                {
                    if( !hasDevice(result.getDevice().toString()))
                    {
                        addDevice(result.getDevice().getAddress(), result.getDevice());
                        if( onCheckModelListener  != null )
                        {
                            onCheckModelListener.scannedDevice(result);
                        }
                    }
                    break;
                }
            }
        }
    };





    /**
     * DO NOT CONNECT DEVICE
     */
    private void addDevice(String address, BluetoothDevice device)
    {
        hashDeviceMap.put(address, device);
    }

    /**
     * DO NOT CONNECT DEVICE
     */
    private boolean hasDevice(String address)
    {
        return hashDeviceMap.get(address) != null;
    }

    /**
     * DO NOT CONNECT DEVICE
     */
    public void onComplete()
    {
        hashDeviceMap.clear();
    }


    /**
     * Connecting Device
     */
    @SuppressLint("MissingPermission")
    public void connGATT(Context context, BluetoothDevice device)
    {
        gattList.add(device.connectGatt(context, true, gattCallback));
    }


    /**
     * Disconnected All Device
     */
    @SuppressLint("MissingPermission")
    public void disconnectGATTAll()
    {
        for (BluetoothGatt bluetoothGatt : gattList) {
            if( bluetoothGatt == null ) continue;
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
        gattList.clear();
    }


    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if( status == BluetoothGatt.GATT_FAILURE ) {
                gatt.disconnect();
                gatt.close();
                hashDeviceMap.remove(gatt.getDevice().getAddress());
                return;
            }
            if( status == 133 ) // Unknown Error
            {
                gatt.disconnect();
                gatt.close();
                hashDeviceMap.remove(gatt.getDevice().getAddress());
                return;
            }
            if( newState == BluetoothGatt.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS)
            {
                // "Connected to " + gatt.getDevice().getName()
                gatt.discoverServices();
            }

        }

        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if( status == BluetoothGatt.GATT_SUCCESS)
            {
                List<BluetoothGattService> services = gatt.getServices();
                for (BluetoothGattService service : services) {
                    // "Found service : " + service.getUuid()
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        //"Found characteristic : " + characteristic.getUuid()
                        if( hasProperty(characteristic, BluetoothGattCharacteristic.PROPERTY_READ))
                        {
                            // "Read characteristic : " + characteristic.getUuid());
                            gatt.readCharacteristic(characteristic);
                        }

                        if( hasProperty(characteristic, BluetoothGattCharacteristic.PROPERTY_NOTIFY))
                        {
                            // "Register notification for characteristic : " + characteristic.getUuid());
                            gatt.setCharacteristicNotification(characteristic, true);
                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if( status == BluetoothGatt.GATT_SUCCESS) {
                if( onReadValueListener == null ) return;
                // This is Background Thread
                mainThreadHandler.post(
                        () ->onReadValueListener.onValue(gatt.getDevice(), onReadValueListener.formatter(characteristic))
                );
            }
        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if( onNotifyValueListener == null ) return;
            // This is Background Thread
            mainThreadHandler.post(
                    ()->onNotifyValueListener.onValue(gatt.getDevice(), onNotifyValueListener.formatter(characteristic))
            );
        }
    };

    public boolean hasProperty(BluetoothGattCharacteristic characteristic, int property)
    {
        int prop = characteristic.getProperties() & property;
        return prop == property;
    }


    public interface OnNotifyValueListener <T>{
        void onValue(BluetoothDevice deivce, T value);
        T formatter(BluetoothGattCharacteristic characteristic);
    }

    public interface OnReadValueListener <T>{
        void onValue(BluetoothDevice deivce, T value);
        T formatter(BluetoothGattCharacteristic characteristic);
    }

    private OnNotifyValueListener onNotifyValueListener = null;
    public BtService setOnNotifyValueListener(OnNotifyValueListener onNotifyValueListener) {
        this.onNotifyValueListener = onNotifyValueListener;
        return this;
    }


    private OnReadValueListener onReadValueListener = null;
    public BtService setOnReadValueListener(OnReadValueListener onReadValueListener) {
        this.onReadValueListener = onReadValueListener;
        return this;
    }

    /**
     * Check model for ScanRecodeData
     */
    public interface OnCheckModelListener {
        boolean isChecked(byte[] bytes);
        void scannedDevice(ScanResult result);
    }
    private OnCheckModelListener onCheckModelListener;
    public BtService setOnCheckModelListener(OnCheckModelListener onCheckModelListener) {
        this.onCheckModelListener = onCheckModelListener;
        return this;
    }
}
