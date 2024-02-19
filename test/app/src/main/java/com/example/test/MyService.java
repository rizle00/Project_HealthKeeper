//package com.example.test;
//
//import android.annotation.SuppressLint;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.Service;
//import android.bluetooth.*;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.*;
//import android.util.Log;
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//import androidx.core.content.ContextCompat;
//import no.nordicsemi.android.ble.BleManager;
//import no.nordicsemi.android.ble.callback.SuccessCallback;
//import no.nordicsemi.android.support.v18.scanner.BuildConfig;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MyService extends Service {
//
//
//    private static final String TAG = "GattService";
//    private Handler handler = new Handler(Looper.getMainLooper());
//    private BroadcastReceiver stateChangedObserver;
//    private BroadcastReceiver bondStateObserver;
//
////    private SendChannel<String> myCharacteristicChangedChannel;
//
//    private final Map<String, ClientManager> clientManagers = new HashMap<>();
//
//    @SuppressLint("ForegroundServiceType")
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        // Setup as a foreground service
//        NotificationChannel notificationChannel = null;
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (notificationManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notificationChannel = new NotificationChannel(
//                        MyService.class.getSimpleName(),
//                        "service name",
//                        NotificationManager.IMPORTANCE_DEFAULT
//                );
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//
//            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, MyService.class.getSimpleName())
//                    .setSmallIcon(R.drawable.ic_launcher_foreground)
//                    .setContentTitle("service")
//                    .setContentText("content")
//                    .setAutoCancel(true);
//
//            startForeground(1, notification.build());
//        }
//
//        // Observe OS state changes in BLE
//        stateChangedObserver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                int bluetoothState = intent.getIntExtra(
//                        BluetoothAdapter.EXTRA_STATE,
//                        -1
//                );
//                switch (bluetoothState) {
//                    case BluetoothAdapter.STATE_ON:
//                        enableBleServices();
//                        break;
//                    case BluetoothAdapter.STATE_OFF:
//                        disableBleServices();
//                        break;
//                }
//            }
//        };
//        bondStateObserver = new BroadcastReceiver() {
//            @SuppressLint("MissingPermission")
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                BluetoothDevice device =
//                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                Log.d(TAG, "Bond state changed for device " + (device != null ? device.getAddress() : "") + ": " + (device != null ? device.getBondState() : ""));
//                if (device != null) {
//                    switch (device.getBondState()) {
//                        case BluetoothDevice.BOND_BONDED:
//                            addDevice(device);
//                            break;
//                        case BluetoothDevice.BOND_NONE:
//                            removeDevice(device);
//                            break;
//                    }
//                }
//            }
//        };
//        ContextCompat.registerReceiver(this, stateChangedObserver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED), ContextCompat.RECEIVER_EXPORTED);
//        ContextCompat.registerReceiver(this, bondStateObserver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED), ContextCompat.RECEIVER_EXPORTED);
//
//        // Startup BLE if we have it
//        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        if (bluetoothManager != null && bluetoothManager.getAdapter() != null && bluetoothManager.getAdapter().isEnabled())
//            enableBleServices();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(stateChangedObserver);
//        unregisterReceiver(bondStateObserver);
//        disableBleServices();
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        if (intent != null && DATA_PLANE_ACTION.equals(intent.getAction())) {
//            return new DataPlane();
//        }
//        return null;
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        if (intent != null && DATA_PLANE_ACTION.equals(intent.getAction())) {
//            myCharacteristicChangedChannel = null;
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * A binding to be used to interact with data of the service
//     */
//    public class DataPlane extends Binder {
////        public void setMyCharacteristicChangedChannel(SendChannel<String> sendChannel) {
////            myCharacteristicChangedChannel = sendChannel;
////        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private void enableBleServices() {
//        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        if (bluetoothManager != null && bluetoothManager.getAdapter() != null && bluetoothManager.getAdapter().isEnabled()) {
//            Log.i(TAG, "Enabling BLE services");
//            for (BluetoothDevice device : bluetoothManager.getAdapter().getBondedDevices()) {
//                addDevice(device);
//            }
//        } else {
//            Log.w(TAG, "Cannot enable BLE services as either there is no Bluetooth adapter or it is disabled");
//        }
//    }
//
//    private void disableBleServices() {
//        for (ClientManager clientManager : clientManagers.values()) {
//            clientManager.close();
//        }
//        clientManagers.clear();
//    }
//
//    private void addDevice(BluetoothDevice device) {
//        if (!clientManagers.containsKey(device.getAddress())) {
//            ClientManager clientManager = new ClientManager(getApplicationContext());
//            clientManager.connect(device).useAutoConnect(true).enqueue();
//            clientManagers.put(device.getAddress(), clientManager);
//        }
//    }
//
//    private void removeDevice(BluetoothDevice device) {
//        clientManagers.remove(device.getAddress());
//    }
//
//    public static final String DATA_PLANE_ACTION = "data-plane";
//
//    private class ClientManager extends BleManager {
//        private BluetoothGattCharacteristic myCharacteristic;
//
//        public ClientManager(@NonNull @NotNull Context context) {
//            super(context);
//        }
//
//
//        @Override
//        public void log(int priority, String message) {
//            if (BuildConfig.DEBUG || priority == Log.ERROR) {
//                Log.println(priority, TAG, message);
//            }
//        }
//
//        @Override
//        protected boolean isRequiredServiceSupported(BluetoothGatt gatt) {
//            BluetoothGattService service = gatt.getService(MyServiceProfile.MY_SERVICE_UUID);
//            if (service != null) {
//                myCharacteristic = service.getCharacteristic(MyServiceProfile.MY_CHARACTERISTIC_UUID);
//                int myCharacteristicProperties = myCharacteristic.getProperties();
//                return (myCharacteristicProperties & BluetoothGattCharacteristic.PROPERTY_READ) != 0 &&
//                        (myCharacteristicProperties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
//            }
//            return false;
//        }
//
//        @Override
//        protected void initialize() {
//            setNotificationCallback(myCharacteristic).with((device, data) -> {
//                if (data.getValue() != null) {
//                    String value = new String(data.getValue(), java.nio.charset.StandardCharsets.UTF_8);
//                    handler.post(() -> {
////                        if (myCharacteristicChangedChannel != null) {
////                            myCharacteristicChangedChannel.send(value);
//                        }
//                    });
//                }
//            });
//
////            beginAtomicRequestQueue()
////                    .add(enableNotifications(myCharacteristic)
////                            .fail((device, status) -> {
////                                log(Log.ERROR, "Could not subscribe: " + status);
////                                disconnect().enqueue();
////                            })
////                    )
////                    .done(new SuccessCallback() {
////                        @Override
////                        public void onRequestCompleted(@NonNull @NotNull BluetoothDevice bluetoothDevice) {
////                            ClientManager.this.log(Log.INFO, "Target initialized");
////                        }
////                    })
////                    .enqueue();
////        }
////
////        @Override
////        protected void onServicesInvalidated() {
////            myCharacteristic = null;
////        }
//    }
//
//    public static class MyServiceProfile {
//        public static final java.util.UUID MY_SERVICE_UUID = java.util.UUID.fromString("80323644-3537-4F0B-A53B-CF494ECEAAB3");
//        public static final java.util.UUID MY_CHARACTERISTIC_UUID = java.util.UUID.fromString("80323644-");
//    }
//}