package com.example.healthkeeper.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.healthkeeper.R;
import com.example.healthkeeper.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    private String msg, title;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "fore Channel";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //token을 서버로 전송
//        sendRegistrationToServer(token);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 포그라운드 서비스에서 사용 중인 노티피케이션 채널 가져오기
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.smallheart);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "fire: " + remoteMessage.getNotification().getTitle());
        title = remoteMessage.getNotification().getTitle();
        msg = remoteMessage.getNotification().getBody();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setContentTitle(title)
                .setContentText(msg)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(contentIntent);
        notificationManager.notify(1001, builder.build());


    }

    //    private static int uniqueRandomValue = new Random().nextInt();

}
