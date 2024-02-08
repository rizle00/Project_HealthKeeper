package com.example.bluetooth;

import android.annotation.SuppressLint;
import android.app.*;
import android.bluetooth.*;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.os.HandlerCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class BtService extends Service {





    public static final int INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701;
    // Bluetooth 관련 변수들
    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private final List<BluetoothGatt> gattList = new ArrayList<>();
    private final HashMap<String, BluetoothDevice> hashDeviceMap = new HashMap<>();
    private final Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    private boolean scanning = false;

    private static final String TAG = BtService.class.getSimpleName();

        private Thread mThread;
    private int mCount = 0;

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {

        public BtService getService() {
            return BtService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("startForeground".equals(intent.getAction())) {
            // 포그라운드 서비스 시작
            startForegroundService();

        } else if (mThread == null) {
            // 스레드 초기화 및 시작
            mThread = new Thread("My Thread") {
                @Override
                public void run() {
                    scanDevices();
                    for (int i = 0; i < 100; i++) {
                        try {
                            mCount++;
                            // 1초 마다 쉬기
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // 스레드에 인터럽트가 걸리면
                            // 오래 걸리는 처리 종료
                            break;
                        }
                        // 1초 마다 로그 남기기
                        Log.d("My Service", "서비스 동작 중 " + mCount);
                    }
                }
            };
            mThread.start();
        }
        return START_NOT_STICKY;
    }

    private void startForegroundService() {
        // default 채널 ID로 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드 서비스");
        builder.setContentText("포그라운드 서비스 실행 중");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        // 오레오에서는 알림 채널을 매니저에 생성해야 한다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // 포그라운드로 시작
        startForeground(1, builder.build());
    }


    // Bluetooth 활성화 여부 체크
    public boolean isOn()
    {
        return adapter.isEnabled();
    }

    // Bluetooth 활성화 요청
    @SuppressLint("MissingPermission")
    public void on(AppCompatActivity activity) {
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, INTENT_REQUEST_BLUETOOTH_ENABLE);
        }
    }

    // Bluetooth 활성화 결과 처리
    public boolean onActivityResult(int requestCode, int resultCode)
    {
        return requestCode == BtService.INTENT_REQUEST_BLUETOOTH_ENABLE
                && resultCode == RESULT_OK;
    }


    // Bluetooth 비활성화
    @SuppressLint("MissingPermission")
    public void off() {
        if (adapter.isEnabled())
            adapter.disable();
    }


    // Bluetooth 장치 스캔 시작
    @SuppressLint("MissingPermission")
    public void scanDevices() { // activity 에서 호출
        if (!adapter.isEnabled()) return;// 어댑터 활성 체크
        if (scanning) return; // 스캐닝 중 .. 리턴

        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();//  스캐너 등록

        mainThreadHandler.postDelayed(() -> {
            scanning = false; // 2분 후 스캔 중지
            scanner.stopScan(callback);
        }, 2 * 60 * 1000);

        scanning = true;// 스캐닝 시작
        scanner.startScan(callback);
    }

    private final ScanCallback callback = new ScanCallback() {
        // 장치 스캔 결과 처리
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            super.onScanResult(callbackType, result);

            Map<ParcelUuid, byte[]> serviceDataMap = result.getScanRecord().getServiceData();
            Log.d(TAG, "onScanResult: "+result.getScanRecord().toString());
            Log.d(TAG, "onScanResult: "+result.getScanRecord().getServiceData().toString());
            if( serviceDataMap == null ) return;
            if( onCheckModelListener == null ) return;
            for( ParcelUuid parcelUuid : serviceDataMap.keySet() )
            {
                if( onCheckModelListener.isChecked(result.getScanRecord().getServiceData(parcelUuid)))
                {
                    if( !hasDevice(result.getDevice().toString()))
                    {
                        addDevice(result.getDevice().getAddress(), result.getDevice());
                        if( onCheckModelListener  != null )
                        {
                            onCheckModelListener.scannedDevice(result);
                        }
                    }
                    break;
                }
            }
        }
    };





    /**
     * DO NOT CONNECT DEVICE
     */
    private void addDevice(String address, BluetoothDevice device)
    {
        hashDeviceMap.put(address, device);
    }

    /**
     * DO NOT CONNECT DEVICE
     */
    private boolean hasDevice(String address)
    {
        return hashDeviceMap.get(address) != null;
    }

    /**
     * DO NOT CONNECT DEVICE
     */
    public void onComplete()
    {
        hashDeviceMap.clear();
    }


    // Bluetooth GATT 연결
    @SuppressLint("MissingPermission")
    public void connGATT(Context context, BluetoothDevice device)
    {
        gattList.add(device.connectGatt(context, true, gattCallback));
    }


    // Bluetooth GATT 연결 해제/
    @SuppressLint("MissingPermission")
    public void disconnectGATTAll()
    {
        for (BluetoothGatt bluetoothGatt : gattList) {
            if( bluetoothGatt == null ) continue;
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
        gattList.clear();
    }

    // Bluetooth GATT 콜백 처리
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")      // 연결 상태 변경 처리
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if( status == BluetoothGatt.GATT_FAILURE ) {
                gatt.disconnect();
                gatt.close();
                hashDeviceMap.remove(gatt.getDevice().getAddress());
                return;
            }
            if( status == 133 ) // Unknown Error
            {
                gatt.disconnect();
                gatt.close();
                hashDeviceMap.remove(gatt.getDevice().getAddress());
                return;
            }
            if( newState == BluetoothGatt.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS)
            {
                // "Connected to " + gatt.getDevice().getName()
                gatt.discoverServices();
            }

        }
        // 서비스 검색 완료 처리
        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if( status == BluetoothGatt.GATT_SUCCESS)
            {
                List<BluetoothGattService> services = gatt.getServices();
                for (BluetoothGattService service : services) {
                    // "Found service : " + service.getUuid()
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        //"Found characteristic : " + characteristic.getUuid()
                        if( hasProperty(characteristic, BluetoothGattCharacteristic.PROPERTY_READ))
                        {
                            // "Read characteristic : " + characteristic.getUuid());
                            gatt.readCharacteristic(characteristic);
                        }

                        if( hasProperty(characteristic, BluetoothGattCharacteristic.PROPERTY_NOTIFY))
                        {
                            // "Register notification for characteristic : " + characteristic.getUuid());
                            gatt.setCharacteristicNotification(characteristic, true);
                        }
                    }
                }
            }
        }
        // 특성 값 읽기 처리
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if( status == BluetoothGatt.GATT_SUCCESS) {
                if( onReadValueListener == null ) return;
                // This is Background Thread
                mainThreadHandler.post(
                        () ->onReadValueListener.onValue(gatt.getDevice(), onReadValueListener.formatter(characteristic))
                );
            }
        }

        // 알림 값 처리
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if( onNotifyValueListener == null ) return;
            // This is Background Thread
            mainThreadHandler.post(
                    ()->onNotifyValueListener.onValue(gatt.getDevice(), onNotifyValueListener.formatter(characteristic))
            );
        }
    };
    // Bluetooth GATT 특성 속성 확인
    public boolean hasProperty(BluetoothGattCharacteristic characteristic, int property)
    {
        int prop = characteristic.getProperties() & property;
        return prop == property;
    }


    public interface OnNotifyValueListener <T>{
        void onValue(BluetoothDevice deivce, T value);
        T formatter(BluetoothGattCharacteristic characteristic);
    }

    public interface OnReadValueListener <T>{
        void onValue(BluetoothDevice deivce, T value);
        T formatter(BluetoothGattCharacteristic characteristic);
    }
    // Bluetooth 알림 값 콜백 설정
    private OnNotifyValueListener onNotifyValueListener = null;
    public BtService setOnNotifyValueListener(OnNotifyValueListener onNotifyValueListener) {
        this.onNotifyValueListener = onNotifyValueListener;
        return this;
    }

    // Bluetooth 읽기 값 콜백 설정
    private OnReadValueListener onReadValueListener = null;
    public BtService setOnReadValueListener(OnReadValueListener onReadValueListener) {
        this.onReadValueListener = onReadValueListener;
        return this;
    }

    // Bluetooth 모델 확인 콜백 설정
    public interface OnCheckModelListener {
        boolean isChecked(byte[] bytes);
        void scannedDevice(ScanResult result);
    }
    private OnCheckModelListener onCheckModelListener;
    public BtService setOnCheckModelListener(OnCheckModelListener onCheckModelListener) {
        this.onCheckModelListener = onCheckModelListener;
        return this;
    }
}
