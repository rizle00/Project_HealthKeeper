package bluetooth;

import android.app.Activity;
import android.content.*;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import com.example.testapplication2.BtService;
import com.example.testapplication2.MyService;
import com.example.testapplication2.TestActivity;

public class ServiceConnector {
    private static final String TAG = ServiceConnector.class.getSimpleName();

    private Context context;
    private BluetoothService bluetoothService;
    private boolean isServiceBound = false;

    public ServiceConnector(Context context) {
        this.context = context;
    }

    // 서비스에 연결하는 메서드
    public void bindService() {
        Intent intent = new Intent(context, BluetoothService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    // 서비스 연결 해제하는 메서드
    public void unbindService() {
        if (isServiceBound) {
            context.unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    // 서비스에 메시지를 보내는 메서드
//    public void sendMessageToService(String message) {
//        if (isServiceBound && bluetoothService != null) {
//            try {
//                bluetoothService.processMessage(message);
//            } catch (RemoteException e) {
//                Log.e(TAG, "Failed to send message to service", e);
//            }
//        }
//    }

    // 서비스 연결 상태를 반환하는 메서드
    public boolean isServiceBound() {
        return isServiceBound;
    }

    // 서비스 커넥션 객체 생성
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothService = ((BluetoothService.LocalBinder) service).getService();
            isServiceBound = true;
            Log.d(TAG, "BluetoothService connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBound = false;
            bluetoothService = null;
            Log.d(TAG, "BluetoothService disconnected");
        }
    };
}
