package com.example.healthkeeper.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BtConManager {

        private BluetoothAdapter bluetoothAdapter;
        private BluetoothDevice targetDevice;
        private Context context;

        public BtConManager(Context context, BluetoothAdapter bluetoothAdapter, BluetoothDevice bluetoothDevice) {
            this.context = context;
            this.bluetoothAdapter = bluetoothAdapter;
            this.targetDevice = bluetoothDevice;
        }

        public void connectToDevice(BluetoothDevice device) {
            targetDevice = device;
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                // Register BroadcastReceiver to listen for Bluetooth connection state changes
                IntentFilter filter = new IntentFilter();
                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                context.registerReceiver(bluetoothReceiver, filter);
            }
        }

        private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                    // Target device disconnected, try to reconnect
                    if (targetDevice != null) {
                        connectToDevice(targetDevice);
                    }
                }
            }
        };

        public void unregisterReceiver() {
            context.unregisterReceiver(bluetoothReceiver);
        }


}
