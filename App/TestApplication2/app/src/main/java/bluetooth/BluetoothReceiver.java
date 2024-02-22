package bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;

public class BluetoothReceiver extends BluetoothGattCallback {

    private static final String TAG = BluetoothReceiver.class.getSimpleName();

    private BluetoothReceiverCallback mCallback;

    public interface BluetoothReceiverCallback {
        void onConnectionStateChanged(int newState);
        void onDataReceived(String data);
    }

    public void setBluetoothReceiverCallback(BluetoothReceiverCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        mCallback.onConnectionStateChanged(newState);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            String receivedData = new String(data);
            mCallback.onDataReceived(receivedData);
        }
    }
}
