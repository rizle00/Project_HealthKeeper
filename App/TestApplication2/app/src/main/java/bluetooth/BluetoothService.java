package bluetooth;

import android.app.Service;
import android.bluetooth.*;
import android.content.*;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.example.testapplication2.BtService;
import com.example.testapplication2.MyService;
import com.example.testapplication2.TestActivity;

import java.util.HashMap;
import java.util.UUID;

public class BluetoothService extends Service {
    private final static String TAG = MyService.class.getSimpleName();

    // Service 바인더 ============================
    public class LocalBinder extends Binder {
        BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    //  서비스 연결 해제 시
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //===========================
    // ble 초기화
    public boolean initialize(Context context) {
        // 여기에 블루투스 초기화 코드 작성
        return true;
    }
    // 블루투스 연결 시도
    public boolean connect(final String address) {
        // 여기에 블루투스 연결 시도 코드 작성
        return true;
    }
    // 블루투스 연결 해제
    public void disconnect() {
        // 여기에 블루투스 연결 해제 코드 작성
    }
    // 데이터에 대한 브로드캐스트 업데이트
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        // 여기에 데이터에 대한 브로드캐스트 업데이트 코드 작성
    }

    // 블루투스에서 받은 데이터를 추출하는 메서드
    public static HashMap<String, String> extractData(String data) {
        HashMap<String, String> extractedData = new HashMap<>();

        // 여기에 데이터 추출하는 코드 작성

        return extractedData;
    }


}