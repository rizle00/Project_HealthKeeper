package bluetooth;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;
import com.example.testapplication2.App;
import com.example.testapplication2.R;

import java.util.HashMap;

public class MyService extends Service {
    private NumberRepository repository;
    private static final String CHANNEL_ID = "fore Channel";
    private NotificationManager notificationManager;
    public MutableLiveData<Integer> progressLiveData = new MutableLiveData<>(0);
    public MyService() {
//  생성자 호출이 안되나봄


    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        repository = new NumberRepository(
                ((App) getApplication()).mainThreadHandler,
                ((App) getApplication()).executorService
        );
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("fore service")
                .setProgress(100, 0, false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            startForeground(2000, notification);
        }

        repository.longTask(result -> {
            if(result instanceof Result.Success){
                if(((Result.Success<Integer>) result).isFinished){
                    stopForeground(true);//포그라운드 종료
                    stopSelf();// 서비스종료
                }
                progressLiveData.postValue(((Result.Success<Integer>) result).data);
                showNotification(((Result.Success<Integer>) result).data);
            } else if(result instanceof Result.Error){
                // 에러
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    public class LocalBinder extends Binder {
        MyService getService() {
            return MyService.this;
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

    @SuppressLint("NotificationPermission")
    private void showNotification(int progress){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("fore service")
                .setProgress(100, progress, false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(2000,notification);
    }
}