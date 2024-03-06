package bluetooth;

import android.annotation.SuppressLint;
import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;
import androidx.core.content.ContextCompat;
import com.example.testapplication2.*;

import java.util.concurrent.Executor;

public class BluetoothService extends Service {
    private final static String TAG = BluetoothService.class.getSimpleName();

    private  Executor executor;
    private boolean mBound, sBound, active; // mBound = bluegatt연결상태, sBound = 서비스 연결상태
    private String deviceAddress;
    private Context mContext;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "fore Channel";
    private BTManger mBtManger;
    private BluetoothAdapter adapter;
    private BluetoothScanner mBtScanner;

   

    private BluetoothConnector mBtConnector;
    private BluetoothRepository repository;


    public BluetoothConnector getmBtConnector() {
        return mBtConnector;
    }// 초기화 위해서 필요

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 서비스 들어옴");
        bluetoothTask();
        startForeground();
        return START_STICKY;
    }

    private void startForeground() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("fore service")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();

        int type = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            type = ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE;
        }
        ServiceCompat.startForeground(this,2000,notification,type);
        active = true;
        Log.d(TAG, "onStartCommand: 포그라운드 서비스 시작?"+type+notification.priority);
    }

    private void createNotificationChannel(){
        Log.d(TAG, "createNotificationChannel: noti");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "fore channel";
            String description = "ground channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = null;


            channel = new NotificationChannel(CHANNEL_ID, name, importance);

            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
    public void stopService(){
        stopForeground(true);
        stopSelf();
        Log.d(TAG, "stopService: 종료됨");
    }

    private void initRepository() {
        repository = new BluetoothRepository(
                executor,
                this
        );
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
        sBound =true;
        Log.d(TAG, "onBind: "+sBound);


        return mBinder;
    }
    //  서비스 연결 해제 시
    @Override
    public boolean onUnbind(Intent intent) {
//       앱 종료시 호출되어버림
        sBound = false;

        Log.d(TAG, "onUnbind: 어플종료됨 ");
        return super.onUnbind(intent);
    }

    public boolean getActive() {
        return active;
    }
    public void setContext(Context context){
        this.mContext = context;
    }
    @Override
    public void onCreate() {
        // 포그라운드 서비스 생성시 작동
        super.onCreate();
        Log.d(TAG, "onCreate: 서비스 생성됨");
        this.executor = ((App) getApplication()).executorService;
        initRepository();
        initialize();
        this.mBtConnector = new BluetoothConnector(mContext, adapter, repository);
        mBound = mBtConnector.isConnected();

    }

    @Override
    public void onDestroy() {
        //서비스 사망시
        Log.d(TAG, "onDestroy: ");
        disconnectGatt();
        active = false;
        super.onDestroy();
    }
    //===========================
    private void bluetoothTask(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(!mBound){// 블루투스 연결 끊겻을때만 시행

                    deviceAddress = mBtManger.checkPairing();// 페어링 된 디바이스가 있으면 주소 반환
                    Log.d(TAG, "run: "+deviceAddress);
                    if(deviceAddress == null) {// 페어링 되어있는지 체크
                        scan();// 되어있지 않을시 스캔
                        connect();
                    } else connect();
                }

            }
        });
    }
    // ble 초기화
    public void initialize() {
        // 여기에 블루투스 초기화 코드 작성
       mBtManger = new BTManger();
        mBtManger.initialize(getApplication());
       adapter = mBtManger.getmBluetoothAdapter();
        Log.d(TAG, "initialize: "+adapter);
    }

    public void scan(){
        mBtScanner = new BluetoothScanner(mContext);
        mBtScanner.startScan();
        deviceAddress = mBtScanner.getDeviceAddress();
    }
    // 블루투스 연결 시도
    public void connect() {
        mBound = mBtConnector.connect(deviceAddress);
        Log.d(TAG, "connect: "+mBound);
        Log.d(TAG, "connect: "+deviceAddress);
    }
    // 블루투스 연결 해제
    public void disconnectGatt() {
        mBtConnector.disconnect();
    }



}