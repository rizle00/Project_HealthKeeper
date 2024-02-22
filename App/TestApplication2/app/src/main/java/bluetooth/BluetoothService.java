package bluetooth;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.*;
import android.content.*;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.example.testapplication2.BtService;
import com.example.testapplication2.MainActivity;
import com.example.testapplication2.MyService;
import com.example.testapplication2.TestActivity;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class BluetoothService extends Service {
    private final static String TAG = MyService.class.getSimpleName();

    private Thread mThread;
    private boolean mBound;
    private boolean isPaired;
    private String deviceAddress;
    private int heart, accident;
    private double temp;

    private BTManger mBtManger;
    private BluetoothScanner mBtScanner;
    private BluetoothConnector mBtConnector;
    private BluetoothReceiver mBtReceiver;
    private BluetoothDataHandler mBtData;

    private BluetoothAdapter mBluetoothAdapter;

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
                    for (int i = 0; i < 100; i++) {
                        try {
                            // 1초 마다 쉬기
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // 스레드에 인터럽트가 걸리면
                            // 오래 걸리는 처리 종료
                            break;
                        }
                        // 1초 마다 로그 남기기
                    }
                }
            };
            mThread.start();
        }
        return START_NOT_STICKY;
    }

    @SuppressLint("ForegroundServiceType")
    private void startForegroundService() {
        // default 채널 ID로 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드 서비스");
        builder.setContentText("포그라운드 서비스 실행 중");
        Intent notificationIntent = new Intent(this, BtActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        // 오레오에서는 알림 채널을 매니저에 생성해야 한다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // 포그라운드로 시작
        startForeground(1, builder.build());
    }

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

    public boolean getBound() {
        return mBound;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");

        // stopService 에 의해 호출 됨
        // 스레드를 정지시킴
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }

        super.onDestroy();
    }
    //===========================
    // ble 초기화
    public boolean initialize(Context context) {
        // 여기에 블루투스 초기화 코드 작성
       mBtManger = new BTManger();

        mBluetoothAdapter = mBtManger.getmBluetoothAdapter();
        isPaired = mBtManger.checkPairing();
        return mBtManger.initialize(getApplication());
    }

    public void scan(Context context){
        mBtScanner = new BluetoothScanner();
        mBtScanner.startScan(context);
        deviceAddress = mBtScanner.getDeviceAddress();
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

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: " + action);
            if (BluetoothAttributes.ACTION_GATT_CONNECTED.equals(action)) {
                mBound = true;
                Log.d(TAG, "onReceive: " + action);
            } else if (BluetoothAttributes.ACTION_GATT_DISCONNECTED.equals(action)) {
                mBound = false;
            } else if (BluetoothAttributes.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                Log.d(TAG, "onReceive: " + action);
            } else if (MyService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, "onReceive: " + action);
                heart = Integer.parseInt(intent.getStringExtra("heart"));
                temp = Double.parseDouble(intent.getStringExtra("temp"));
                accident = Integer.parseInt(intent.getStringExtra("accident"));
            }
        }
    };




}