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
    private BluetoothScanner mBluetoothScanner;
    private Context context;

    private Set<BluetoothDevice> deviceList;
    private final static String deviceName = "HM10";

    public BTManger(Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
    }



    public boolean isEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    @SuppressLint("MissingPermission")
    public void requestActivation(Activity activity, int requestCode) {
        if (!isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, requestCode);
        }
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
        mBluetoothScanner = new BluetoothScanner(deviceList, deviceName);
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        } else if(!mBluetoothScanner.checkPairing()) mBluetoothScanner.startScan(context);

        return true;
    }

}
