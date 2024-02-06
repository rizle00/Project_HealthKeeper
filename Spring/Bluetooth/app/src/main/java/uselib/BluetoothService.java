package uselib;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.example.bluetooth.R;
import kotlinx.coroutines.channels.SendChannel;

import java.util.HashMap;
import java.util.UUID;

public class BluetoothService extends Service {

    private final String TAG = "gatt-service";

    private BroadcastReceiver stateChangedObserver;
    private BroadcastReceiver bondStateObserver;

    private SendChannel<String> myCharacteristicChangedChannel;



    private final Map<String, ClientManager> clientManagers = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        // Setup as a foreground service
        NotificationChannel notificationChannel = new NotificationChannel(
                BluetoothService.class.getSimpleName(),
                getResources().getString(R.string.gatt_service_name),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager notificationService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationService.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, BluetoothService.class.getSimpleName())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getResources().getString(R.string.gatt_service_name))
                .setContentText(getResources().getString(R.string.gatt_service_running_notification))
                .setAutoCancel(true);

        startForeground(1, notification.build());

        // Observe OS state changes in BLE
        stateChangedObserver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int bluetoothState = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        -1
                );
                switch (bluetoothState) {
                    case BluetoothAdapter.STATE_ON:
                        enableBleServices();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        disableBleServices();
                        break;
                }
            }
        };
        bondStateObserver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BluetoothDevice device =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "Bond state changed for device " + device.getAddress() + ": " + device.getBondState());
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDED:
                        addDevice(device);
                        break;
                    case BluetoothDevice.BOND_NONE:
                        removeDevice(device);
                        break;
                }
            }
        };
        ContextCompat.registerReceiver(this, stateChangedObserver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED), ContextCompat.RECEIVER_EXPORTED);
        ContextCompat.registerReceiver(this, bondStateObserver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED), ContextCompat.RECEIVER_EXPORTED);

        // Startup BLE if we have it
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager.getAdapter().isEnabled()) enableBleServices();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stateChangedObserver);
        unregisterReceiver(bondStateObserver);
        disableBleServices();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(DATA_PLANE_ACTION)) {
            return new DataPlane();
        }
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(DATA_PLANE_ACTION)) {
            myCharacteristicChangedChannel = null;
            return true;
        }
        return false;
    }

    /**
     * A binding to be used to interact with data of the service
     */
    public class DataPlane extends Binder {
        public void setMyCharacteristicChangedChannel(SendChannel<String> sendChannel) {
            myCharacteristicChangedChannel = sendChannel;
        }
    }

    private void enableBleServices() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager.getAdapter().isEnabled()) {
            Log.i(TAG, "Enabling BLE services");
            for (BluetoothDevice device : bluetoothManager.getAdapter().getBondedDevices()) {
                addDevice(device);
            }
        } else {
            Log.w(TAG, "Cannot enable BLE services as either there is no Bluetooth adapter or it is disabled");
        }
    }

    private void disableBleServices() {
        for (ClientManager clientManager : clientManagers.values()) {
            clientManager.close();
        }
        clientManagers.clear();
    }

    private void addDevice(BluetoothDevice device) {
        if (!clientManagers.containsKey(device.getAddress())) {
            ClientManager clientManager = new ClientManager();
            clientManager.connect(device).useAutoConnect(true).enqueue();
            clientManagers.put(device.getAddress(), clientManager);
        }
    }

    private void removeDevice(BluetoothDevice device) {
        clientManagers.remove(device.getAddress()).close();
    }

    /*
     * Manages the entire GATT service, declaring the services and characteristics on offer
     */
    public static class MyServiceProfile {
        public static final UUID MY_SERVICE_UUID = UUID.fromString("80323644-3537-4F0B-A53B-CF494ECEAAB3");
        public static final UUID MY_CHARACTERISTIC_UUID = UUID.fromString("80323644-3537-4F0B-A53B-CF494ECEAAB3");
    }
    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

    }
}