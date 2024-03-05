package bluetooth;

import android.annotation.SuppressLint;
import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;
import com.example.testapplication2.*;

import java.util.concurrent.Executor;

public class BluetoothService extends Service {
    private final static String TAG = BluetoothService.class.getSimpleName();

    private Handler resultHandler;
    private  Executor executor;
    private boolean mBound, sBound; // mBound = bluegatt연결상태, sBound = 서비스 연결상태
    private String deviceAddress;
    private Context mContext;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "fore Channel";
    private BTManger mBtManger;
    private BluetoothAdapter adapter;
    private BluetoothScanner mBtScanner;

    public BluetoothConnector getmBtConnector() {
        return mBtConnector;
    }

    private BluetoothConnector mBtConnector;
    private BluetoothRepository repository;




    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        this.resultHandler = ((App) getApplication()).mainThreadHandler;
        this.executor = ((App) getApplication()).executorService;
        initRepository();
        initialize();
        this.mBtConnector = new BluetoothConnector(mContext, adapter, repository);
        mBound = mBtConnector.isConnected();

        bluetoothTask();

//        if(mBound) {



        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("fore service")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
//            startForeground(2000, notification, Service.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
//
//        }

//        }
//        Context context = getApplicationContext();
//         intent = new Intent(context,BluetoothService.class); // Build the intent for the service
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intent);
//        }
        int type = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            type = ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE;
        }
        ServiceCompat.startForeground(this,2000,notification,type);

        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel(){
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

    private void initRepository() {
        repository = new BluetoothRepository(
//               resultHandler,
                executor,
                this
        );
    }

    @SuppressLint("ForegroundServiceType")
    public void startForegroundService() {
        // default 채널 ID로 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("포그라운드 서비스");
        builder.setContentText("포그라운드 서비스 실행 중");
        // 클릭시 실행할 액티비티 설정
        Intent notificationIntent = new Intent(this, BtActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // 오레오에서는 알림 채널을 매니저에 생성해야 한다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // 포그라운드로 시작
        startForeground(1, builder.build());
        Log.d(TAG, "startForegroundService: 포그라운드 시작됨");
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
//        unregisterReceiver(mGattUpdateReceiver);
//        stopForeground(true);//포그라운드 종료
//        stopSelf();// 서비스종료
        disconnectGatt();
//        sBound =false;
        return super.onUnbind(intent);
    }

    public boolean getBound() {
        return sBound;
    }
    public void setContext(Context context){
        this.mContext = context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 서비스 생성됨");
        Log.d(TAG, "onCreate: ");

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");


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
//        mBtConnector = new BluetoothConnector(mContext, adapter, repository);
        mBound = mBtConnector.connect(deviceAddress);
        Log.d(TAG, "connect: "+mBound);
        Log.d(TAG, "connect: "+deviceAddress);
    }
    // 블루투스 연결 해제
    public void disconnectGatt() {
        mBtConnector.disconnect();
    }



}