package com.example.btapplication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.data.Data;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MyBleManager extends BleManager {

    private static final String TAG = "BLEConnector";

    private static final UUID SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    private static final UUID WRITE_CHAR = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    private static final UUID READ_CHAR = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    private BluetoothGattCharacteristic readCharacteristic;
    private BluetoothGattCharacteristic writeCharacteristic;

    public MyBleManager(@NonNull final Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return new MyManagerGattCallback();
    }

    @Override
    public void log(final int priority, @NonNull final String message) {
        Log.println(priority, "BLEConnector", message);
    }

    private class MyManagerGattCallback extends BleManagerGattCallback {
        @Override
        public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(SERVICE_UUID);
            if (service != null) {
                readCharacteristic = service.getCharacteristic(READ_CHAR);
                writeCharacteristic = service.getCharacteristic(WRITE_CHAR);
            }

            boolean notify = false;

            if (readCharacteristic != null) {
                final int properties = readCharacteristic.getProperties();
                notify = (properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
            }

            return readCharacteristic != null && writeCharacteristic != null && notify;
        }

        @Override
        protected boolean isOptionalServiceSupported(@NonNull final BluetoothGatt gatt) {
            return super.isOptionalServiceSupported(gatt);
        }

        @Override
        protected void initialize() {
            beginAtomicRequestQueue()
                    .add(requestMtu(247) // Remember, GATT needs 3 bytes extra. This will allow packet size of 244 bytes.
                            .with((device, mtu) -> log(Log.INFO, "MTU set to " + mtu))
                            .fail((device, status) -> log(Log.WARN, "Requested MTU not supported: " + status)))
                    .add(enableNotifications(readCharacteristic))
                    .add(enableNotifications(writeCharacteristic))
                    .done(device -> log(Log.INFO, "Target initialized"))
                    .enqueue();

            // Set a callback for your notifications. You may also use waitForNotification(...).
            // Both callbacks will be called when notification is received.
            setNotificationCallback(readCharacteristic).with(new DataReceiveHandler());

            enableNotifications(readCharacteristic)
                    .done(device -> log(Log.INFO, "Read notifications enabled"))
                    .fail((device, status) -> log(Log.WARN, "Read characteristic not found"))
                    // PHY 요청부분 삭제
                    .enqueue();

            enableNotifications(writeCharacteristic)
                    .done(device -> log(Log.INFO, "Write notifications enabled"))
                    .fail((device, status) -> log(Log.WARN, "Write characteristic not found"))
                    .enqueue();

        }

        @Override
        protected void onDeviceDisconnected() {
            log(Log.INFO, "Target disconnected");
        }

        @Override
        protected void onServicesInvalidated() {

        }
    }

    private class DataReceiveHandler implements ProfileDataCallback {
        @Override
        public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
            String receiveInfo = device.getAddress() + "#" + data.getStringValue(0); // byte로 받을 때 getValue로 받으면 byte
            Log.d(TAG, receiveInfo);
        }
    }

    public void writeCharacter(String data) {
        if (writeCharacteristic == null) {
            return;
        }

        writeCharacteristic(
                writeCharacteristic,
                data.getBytes(StandardCharsets.UTF_8),
                BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                .enqueue();
    }

    public void abort() {
        cancelQueue();
    }

}
