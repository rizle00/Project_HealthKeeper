package com.example.healthkeeper.bluetooth;

import android.annotation.SuppressLint;
import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;
import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.firebase.AlarmActivity;
import com.example.healthkeeper.main.MainActivity;

import java.util.concurrent.Executor;

public class BluetoothService extends Service {
    private final static String TAG = BluetoothService.class.getSimpleName();

    private Executor executor;
    private boolean mBound, sBound, active; // mBound = bluegatt연결상태, sBound = 서비스 연결상태
    private String deviceAddress;
//    boolean isPatient;
//    private Context mContext;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "fore Channel";
    private BTManger mBtManger;
    private BluetoothAdapter adapter;
    private BluetoothScanner mBtScanner;

    private BluetoothConnector mBtConnector;
    private BluetoothRepository repository;
    private BluetoothViewModel viewModel;
    private NotificationCompat.Builder builder;
    private SharedPreferences pref;
    private boolean isPatient;


    public BluetoothConnector getmBtConnector() {
        return mBtConnector;
    }// 초기화 위해서 필요

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 서비스 들어옴");

        startForeground();
        if (isPatient) {
            bluetoothTask();
            observe();
        }

        return START_STICKY;
    }


    private void startForeground() {
        Intent intent = new Intent(this, MainActivity.class);
        Notification notification =
                builder.setContentText("모니터링 중..")
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setOngoing(true)
                        .setContentIntent(getPendingIntent(intent))
                        .build();


        int type = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            type = ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE;
        }
        ServiceCompat.startForeground(this, 2000, notification, type);
        active = true;
        Log.d(TAG, "onStartCommand: 포그라운드 서비스 시작?" + type);
    }

    private PendingIntent getPendingIntent(Intent intent) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1001, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        return pendingIntent;
    }


    private void createNotificationChannel() {
        Log.d(TAG, "createNotificationChannel: noti");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification channel";
            String description = "ground channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = null;


            channel = new NotificationChannel(CHANNEL_ID, name, importance);

            channel.setDescription(description);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.smallheart)
                    .setContentTitle("Health Keeper");
        }
    }

    public void stopService() {
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
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        sBound = true;
        Log.d(TAG, "onBind: " + sBound);


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

//    public void setContext(Context context) {
//        this.mContext = context;
//    }

    @Override
    public void onCreate() {
        // 포그라운드 서비스 생성시 작동
        super.onCreate();
        Log.d(TAG, "onCreate: 서비스 생성됨");
        this.executor = ((App) getApplication()).executorService;
        initRepository();
        initialize();
        // 뷰모델 초기화
        viewModel = ((App) getApplicationContext()).getSharedViewModel();

        observe();
        pref = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
        isPatient = pref.getString("role","").equals("patient");
       String id = pref.getString("user_id", "");
        this.mBtConnector = new BluetoothConnector(getApplicationContext(), adapter, repository, viewModel, id);
        mBound = mBtConnector.isConnected();
        active = true;

        createNotificationChannel();

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
    private void bluetoothTask() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (!mBound) {// 블루투스 연결 끊겻을때만 시행

                    deviceAddress = mBtManger.checkPairing();// 페어링 된 디바이스가 있으면 주소 반환
                    Log.d(TAG, "run: " + deviceAddress);
                    if (deviceAddress == null) {// 페어링 되어있는지 체크
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
        Log.d(TAG, "initialize: " + adapter);
    }

    public void scan() {
        mBtScanner = new BluetoothScanner(getApplicationContext());
        mBtScanner.startScan();
        deviceAddress = mBtScanner.getDeviceAddress();
    }

    // 블루투스 연결 시도
    public void connect() {
        mBound = mBtConnector.connect(deviceAddress);
        Log.d(TAG, "connect: " + mBound);
        Log.d(TAG, "connect: " + deviceAddress);
    }

    // 블루투스 연결 해제
    public void disconnectGatt() {
        mBtConnector.disconnect();
    }

    private void observe() {

//        viewModel.getHeartLiveData().observeForever(heart -> {
//            viewModel.getTempLiveData().observeForever(temp -> {
//                viewModel.getAccidentLiveData().observeForever(accident -> {
//                    handleAccidentDetected(heart, temp, accident);
//                    Log.d(TAG, "observe: "+heart+"t"+temp);
//                });
//            });
//        }); 맵으로 처리
        viewModel.getData().observeForever(data->{
            if(data != null){
                int heart = (int) data.get("heart");
                double temp = (double) data.get("temp");
                String accident = (String) data.get("accident");
                handleAccidentDetected(heart, temp, accident);
            }

        });

    }

    private void handleAccidentDetected(Integer heart, Double temp, String accident) {

        //7 -> 낙상 8-> 맥박상승 9 -> 맥박하락 10 -> 체온상승 11-> 체온하락
        if (heart != 0 && temp != 0) {
            //앱 노티 용
            String contents[] = {"낙상이 발생했습니까? 확인해주세요",
                    "심박이 너무 높습니다. 확인해주세요", "심박이 너무 낮습니다. 확인해주세요",
                    "체온이 너무 높습니다. 확인해주세요", "체온이 너무 낮습니다. 확인해주세요"
            };
            // sms 보내기용
            String texts[] = {"낙상이 발생했습니다",
                    "심박이 너무 높습니다", "심박이 너무 낮습니다",
                    "체온이 너무 높습니다", "체온이 너무 낮습니다"
            };
            Intent intent = new Intent(this, AlarmActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


//            builder.setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setOngoing(true)
//                    .setVibrate(new long[]{1000, 1000, 1000,
//                            1000, 1000});
            if (accident.equals("1")) {
                //낙상발생

                intent.setType("7");
                intent.putExtra("content", contents[0]);
                intent.putExtra("text", texts[0]);
//                intent.putExtra("notifyId", 1001);
//                builder.setContentText(contents[0])
//                        .setContentIntent(getPendingIntent(intent));
//                notificationManager.notify(1001, builder.build());

                this.startActivity(intent);
                startActivity(intent);
            } else {
                if (heart > 160) {
                    //심박이 높음

                    intent.setType("8");
                    intent.putExtra("content", contents[1]);
                    intent.putExtra("text", texts[1]);
//                    intent.putExtra("notifyId", 1002);
//                    builder.setContentText(contents[1])
//                            .setContentIntent(getPendingIntent(intent));
//                    notificationManager.notify(1002, builder.build());
                    startActivity(intent);
                } else if (heart < 60) {
                    //심박이 낮음
                    intent.setType("9");
                    intent.putExtra("content", contents[2]);
                    intent.putExtra("text", texts[2]);
//                    intent.putExtra("notifyId", 1003);
//                    builder.setContentText(contents[2])
//                            .setContentIntent(getPendingIntent(intent));
//                    notificationManager.notify(1003, builder.build());
                    startActivity(intent);
                }
                if (temp > 37.5) {
                    //체온이높음
                    intent.setType("10");
                    intent.putExtra("content", contents[3]);
                    intent.putExtra("text", texts[3]);
//                    intent.putExtra("notifyId", 1004);
//                    builder.setContentText(contents[3])
//                            .setContentIntent(getPendingIntent(intent));
//                    notificationManager.notify(1004, builder.build());
                    startActivity(intent);
                } else if (temp < 35.5) {
                    //체온이 낮음
                    intent.setType("11");
                    intent.putExtra("content", contents[4]);
                    intent.putExtra("text", texts[4]);
//                    intent.putExtra("notifyId", 1005);
//                    builder.setContentText(contents[4])
//                            .setContentIntent(getPendingIntent(intent));
//                    notificationManager.notify(1005, builder.build());
                    startActivity(intent);
                }
            }
        }
    }


    public static void showNotification(Context context, String title,
                                        String message) {
//        Intent intent
//                = new Intent(context, MainAlarmHistoryActivity.class);
//        intent.putExtra("addFriend", true);
//        intent.putExtra("title", title);
//        intent.putExtra("message", message);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


//                .setCustomContentView(getCustomDesign(context, title, message));
    }
}