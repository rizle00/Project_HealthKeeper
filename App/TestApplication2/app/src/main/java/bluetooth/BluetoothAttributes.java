package bluetooth;

public class BluetoothAttributes {

    public final static String SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public final static String CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public final static String CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    public final static String DEVICE_UUID = "0000180A-0000-1000-8000-00805f9b34fb";

    // Bluetooth 연결 상태 상수
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public final static String deviceName = "HM10";

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
}
