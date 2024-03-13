package com.example.healthkeeper.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.clj.fastble.BleManager;
import lombok.Getter;

import java.util.Set;


public class BTManger {
    private static final String TAG = BluetoothManager.class.getSimpleName();

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    private BluetoothAdapter mBluetoothAdapter;

    @SuppressLint("MissingPermission")
    public boolean initialize(Application application) {
        BleManager.getInstance().init(application);
        BluetoothManager mBluetoothManager = BleManager.getInstance().getBluetoothManager();
        // 매니저 체크
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) application.getSystemService(Context.BLUETOOTH_SERVICE);
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
        }

        return true;
    }

    @SuppressLint("MissingPermission")
    public String checkPairing() {
        if (mBluetoothAdapter.getBondedDevices() != null) {
            Set<BluetoothDevice> deviceList = mBluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : deviceList) {
                if (device.getName() != null && device.getName().equals(BluetoothAttributes.deviceName)) {
                    Log.d(TAG, "checkPairing: " + device.getName());
                    Log.d(TAG, "checkPairing: " + device.getAddress());
                    return device.getAddress();
                }
            }
        }
        return null;
    }

}
