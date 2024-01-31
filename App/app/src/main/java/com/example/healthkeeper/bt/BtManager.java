package com.example.healthkeeper.bt;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class BtManager {
        private final Context context;
        private final BluetoothManager bluetoothManager;
        private final android.bluetooth.BluetoothAdapter bluetoothAdapter;
        private final android.bluetooth.le.BluetoothLeScanner bluetoothLeScanner;
        private ArrayList<DeviceData> scanList;
        private BtInterface connectedStateObserver;
        private BluetoothGatt bleGatt;


        public BtManager(Context context) {
            this.context = context;
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = Objects.requireNonNull(bluetoothManager).getAdapter();
            bluetoothLeScanner = Objects.requireNonNull(bluetoothAdapter).getBluetoothLeScanner();
        }

        private final ScanCallback scanCallback = new ScanCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                Log.d("onScanResult", result.toString());
                if (result.getDevice().getName() != null) {
                    String uuid = "null";

                    if (result.getScanRecord() != null && result.getScanRecord().getServiceUuids() != null) {
                        uuid = Objects.requireNonNull(result.getScanRecord().getServiceUuids()).toString();
                    }

                    DeviceData scanItem = new DeviceData(
                            result.getDevice().getName() != null ? result.getDevice().getName() : "null",
                            uuid,
                            result.getDevice().getAddress() != null ? result.getDevice().getAddress() : "null"
                    );

                    if (scanList != null && !scanList.contains(scanItem)) {
                        scanList.add(scanItem);
                    }
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.println(Log.DEBUG, "onScanFailed", "onScanFailed  " + errorCode);
            }
        };

        private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("BleManager", "연결 성공");
                    gatt.discoverServices();
                    if (connectedStateObserver != null) {
                        connectedStateObserver.onConnectedStateObserve(true, "onConnectionStateChange:  STATE_CONNECTED\n---");
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("BleManager", "연결 해제");
                    if (connectedStateObserver != null) {
                        connectedStateObserver.onConnectedStateObserve(false, "onConnectionStateChange:  STATE_DISCONNECTED\n---");
                    }
                }
            }

            @SuppressLint("MissingPermission")
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    bleGatt = gatt;
                    Toast.makeText(context, gatt != null ? " " + gatt.getDevice().getName() + " 연결 성공" : "", Toast.LENGTH_SHORT).show();
                    StringBuilder sendText = new StringBuilder("onServicesDiscovered:  GATT_SUCCESS\n                         ↓\n");

                    for (android.bluetooth.BluetoothGattService service : Objects.requireNonNull(gatt).getServices()) {
                        sendText.append("- ").append(service.getUuid().toString()).append("\n");
                        for (android.bluetooth.BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                            sendText.append("       ").append(characteristic.getUuid().toString()).append("\n");
                        }
                    }
                    sendText.append("---");
                    if (connectedStateObserver != null) {
                        connectedStateObserver.onConnectedStateObserve(true, sendText.toString());
                    }
                }
            }
        };

        @SuppressLint("MissingPermission")
        public void startBleScan() {
            if (scanList != null) {
                scanList.clear();
            }

            ScanSettings scanSettings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                    .build();

            Objects.requireNonNull(bluetoothLeScanner).startScan(null, scanSettings, scanCallback);
        }

        @SuppressLint("MissingPermission")
        public void stopBleScan() {
            if (bluetoothLeScanner != null) {
                bluetoothLeScanner.stopScan(scanCallback);
            }
        }

        @SuppressLint("MissingPermission")
        public void startBleConnectGatt(DeviceData deviceData) {
            Objects.requireNonNull(bluetoothAdapter).getRemoteDevice(deviceData.getAddress()).connectGatt(context, false, gattCallback);
        }

        public void setScanList(ArrayList<DeviceData> pScanList) {
            scanList = pScanList;
        }

        public void onConnectedStateObserve(BtInterface pConnectedStateObserver) {
            connectedStateObserver = pConnectedStateObserver;
        }
    }


