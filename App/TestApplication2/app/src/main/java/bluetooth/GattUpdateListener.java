package bluetooth;

import android.content.Intent;

public interface GattUpdateListener {
    void onGattConnected();
    void onGattDisconnected();
    void onGattServicesDiscovered();
    void onDataAvailable(Intent intent);
}
