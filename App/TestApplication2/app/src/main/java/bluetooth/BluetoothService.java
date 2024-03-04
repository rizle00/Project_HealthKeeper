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
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import com.example.testapplication2.*;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class BluetoothService extends Service implements GattUpdateListener {
    private final static String TAG = BluetoothService.class.getSimpleName();

    private Handler resultHandler;
    private  Executor executor;
    private boolean mBound, sBound; // mBound = bluegatt연결상태, sBound = 서비스 연결상태
    private String deviceAddress;

    private int heart, accident;
    private double temp;





    private Context mContext;
    private BTManger mBtManger;
    private BluetoothAdapter adapter;
    private BluetoothScanner mBtScanner;

    public BluetoothConnector getmBtConnector() {
        return mBtConnector;
    }

    private BluetoothConnector mBtConnector;
    private BluetoothReceiver mBtReceiver;
    private BluetoothRepository repository;




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        initRepository();

//        bluetoothTask();
//        if ("startForeground".equals(intent.getAction())) {
//            // 포그라운드 서비스 시작
//            startForegroundService();
//
//        }
//        else if (mThread == null) {
//            // 스레드 초기화 및 시작
//            mThread = new Thread("My Thread") {
//                @Override
//                public void run() {
//                    for (int i = 0; i < 100; i++) {
//                        try {
//                            // 1초 마다 쉬기
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            // 스레드에 인터럽트가 걸리면
//                            // 오래 걸리는 처리 종료
//                            break;
//                        }
//                        // 1초 마다 로그 남기기
//                    }
//                }
//            };
//            mThread.start();
//        }
        return START_NOT_STICKY;
    }

    private void initRepository() {
        repository = new BluetoothRepository(
//                ((App) getApplication()).mainThreadHandler,
                executor,
                this
        );
    }

    @SuppressLint("ForegroundServiceType")
    private void startForegroundService() {
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
    }

    @Override
    public void onGattConnected() {
        // 블루투스 연결 상태에 따른 처리
        mBound = true;
        Log.d(TAG, "Bluetooth connected");
    }

    @Override
    public void onGattDisconnected() {
// 블루투스 연결 해제 상태에 따른 처리
        mBound = false;
        Log.d(TAG, "Bluetooth disconnected");
    }

    @Override
    public void onGattServicesDiscovered() {
        // GATT 서비스 발견 상태에 따른 처리
        Log.d(TAG, "Gatt services discovered");
    }

    @Override
    public void onDataAvailable(Intent intent) {
//        handleData(intent);
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
        Log.d(TAG, "onBind: 바인드됨");
        sBound =true;
        this.resultHandler = ((App) getApplication()).mainThreadHandler;
        this.executor = ((App) getApplication()).executorService;
        initRepository();
        initialize();
        this.mBtConnector = new BluetoothConnector(mContext, adapter, repository);


//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter(), RECEIVER_EXPORTED);
//            }
        bluetoothTask();
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
        return mBound;
    }
    public void setContext(Context context){
        this.mContext = context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 서비스 생성됨");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter(), RECEIVER_EXPORTED);
//
//        }
        Log.d(TAG, "onCreate: ");

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");

        // stopService 에 의해 호출 됨
        // 스레드를 정지시킴
//        if (mThread != null) {
//            mThread.interrupt();
//            mThread = null;
//        }

        super.onDestroy();
    }
    //===========================
    public void bluetoothTask(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(!mBound){// 블루투스 연결 끊겻을때만 시행

                    initialize();// 블루투스 초기화
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
            } else if (BluetoothAttributes.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, "onReceive: " + action);
//               handleData(intent);
            }
        }
    };

//    private void handleData(Intent intent){
//
//        heart = Integer.parseInt(intent.getStringExtra("heart"));
//        temp = Double.parseDouble(intent.getStringExtra("temp"));
//        accident = Integer.parseInt(intent.getStringExtra("accident"));
//        heartLiveData.setValue(heart);
//        tempLiveData.setValue(temp);
//        accidentLiveData.setValue(accident);
//
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("heart", heart);
//        map.put("temp", temp);
//        map.put("accident", accident);// 0 -> 문제없음 , 1 -> 문제발생
//        repository.insertData(map, result -> {
//            if(result instanceof Result.Success){
//                Log.d(TAG, "handleData: "+((Result.Success<String>) result).data);
//            } else if(result instanceof Result.Error){
//                // 에러
//            }
//        });
////        String json = new Gson().toJson(map);
//
//    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAttributes.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothAttributes.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothAttributes.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothAttributes.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }




}