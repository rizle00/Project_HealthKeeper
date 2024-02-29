package bluetooth;

import android.content.Intent;

public class BluetoothHandler {
    private GattUpdateListener mListener;
      public void setGattUpdateListener(GattUpdateListener listener) {
        mListener = listener;
    }

    // 리스너 호출 메소드들
    private void notifyGattConnected() {
        if (mListener != null) {
            mListener.onGattConnected();
        }
    }

    private void notifyGattDisconnected() {
        if (mListener != null) {
            mListener.onGattDisconnected();
        }
    }

    private void notifyGattServicesDiscovered() {
        if (mListener != null) {
            mListener.onGattServicesDiscovered();
        }
    }

    private void notifyDataAvailable(Intent intent) {
        if (mListener != null) {
            mListener.onDataAvailable(intent);
        }
    }
}
