package com.example.healthkeeper.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.util.ArrayList;
import java.util.List;

public class BluetoothScanner {
    private final static String TAG = BluetoothScanner.class.getSimpleName();
    private final Context mContext;
    private String deviceAddress;

    public BluetoothScanner(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }


    // 스캔 룰 지정, 기기이름(배열가능), 맥주소, 등..
    private void setScanRule() {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setDeviceName(true, BluetoothAttributes.deviceName)
                .setAutoConnect(true)
                .setScanTimeOut(10 * 1000)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    // 스캔 시작
    public void startScan() {
        // 기기이름 배열로 추가 가능, 맥주소 등  세팅가능
        setScanRule();

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Toast.makeText(mContext, "스캔을 시작합니다", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onScanning(BleDevice bleDevice) {

                if (bleDevice.getName().equals(BluetoothAttributes.deviceName)) {
                    BleManager.getInstance().cancelScan();
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                showDeviceList(scanResultList);
            }
        });
    }
    // 스캔 결과 보여주기
    private void showDeviceList(List<BleDevice> scanResultList) {
        if (scanResultList.size() == 0) { // 스캔된 장치가 없는 경우.
            Toast.makeText(mContext.getApplicationContext(), "근처에 연결 가능한 장치가 없습니다. 재검색을 시도합니다.", Toast.LENGTH_LONG).show();

            startScan();
        } else {// 스캔된 장치가 있는 경우.

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("블루투스 장치 선택");

            // 각 디바이스는 이름과(서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다.
            List<String> listItems = new ArrayList<String>();

            //페어링된 기기 쿼리
            for (BleDevice device : scanResultList) {
                // device.getName() : 단말기의 Bluetooth Adapter 이름을 반환.
                listItems.add(device.getName());
//                scanedList.add(device);
            }

            listItems.add("취소");  // 취소 항목 추가.

            // CharSequence : 변경 가능한 문자열.
            // toArray : List형태로 넘어온것 배열로 바꿔서 처리하기 위한 toArray() 함수.
            final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
            // toArray 함수를 이용해서 size만큼 배열이 생성 되었다.
            listItems.toArray(new CharSequence[listItems.size()]);

            builder.setItems(items, new DialogInterface.OnClickListener() {

                @SuppressLint("MissingPermission")
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (item == scanResultList.size()) { // 연결할 장치를 선택하지 않고 '취소' 를 누른 경우.
                        Toast.makeText(mContext, "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                        //                    finish();
                    } else { // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함.
                        BleDevice selectedDevice = null;
                        //  선택한 기기를 가져옴
                        for (BleDevice device : scanResultList) {
                            if (device.getName().equals(items[item])) {
                                selectedDevice = device;
                                break;
                            }
                        }
                        if (selectedDevice != null) {
                            BluetoothDevice device = selectedDevice.getDevice();
                            Log.d(TAG, "onClick: "+device.getName());
                            if(device.createBond())
                                deviceAddress = device.getAddress();
                        }

                    }
                }

            });

            builder.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
            AlertDialog alert = builder.create();
            alert.show();

        }

    }
}
