package bluetooth;

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

import java.util.Set;

public class BTManger {
    private static final String TAG = BluetoothManager.class.getSimpleName();

    private BluetoothManager mBluetoothManager;

    private BluetoothAdapter mBluetoothAdapter;

    private Set<BluetoothDevice> deviceList;
    private final static String deviceName = "HM10";

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    @SuppressLint("MissingPermission")
    public boolean initialize(Application application) {
        BleManager.getInstance().init(application);
        mBluetoothManager = BleManager.getInstance().getBluetoothManager();
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
        deviceList = mBluetoothAdapter.getBondedDevices();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    @SuppressLint("MissingPermission")
    public boolean checkPairing() {

        for (BluetoothDevice device : deviceList) {
            if (device.getName() != null && device.getName().equals(deviceName)) {
                return true;
            }
        }
        return false;
    }

}
