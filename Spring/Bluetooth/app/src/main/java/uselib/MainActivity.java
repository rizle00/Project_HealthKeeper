package uselib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.example.bluetooth.R;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.Channel;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final CoroutineScope defaultScope = new CoroutineScope(Dispatchers.Default);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private GattServiceConn gattServiceConn;
    private GattService.DataPlane gattServiceData;
    private final Channel<String> myCharacteristicValueChangeNotifications = new Channel<>();

    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            this::handlePermissionsResult
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView gattCharacteristicValue = findViewById(R.id.textViewGattCharacteristicValue);

        defaultScope.launch(() -> {
            for (String newValue : myCharacteristicValueChangeNotifications) {
                mainHandler.post(() -> {
                    gattCharacteristicValue.setText(newValue);
                });
            }
        });

        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        } else {
            permissions = new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
            };
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startGattService();
        } else {
            requestPermissionLauncher.launch(permissions);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        gattServiceConn = new GattServiceConn();
        Intent serviceIntent = new Intent(this, GattService.class);
        bindService(serviceIntent, gattServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (gattServiceConn != null) {
            unbindService(gattServiceConn);
            gattServiceConn = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, GattService.class));
    }

    private void handlePermissionsResult(@NonNull Map<String, Boolean> permissions) {
        if (permissions.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false) ||
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
            startGattService();
        } else {
            finish();
        }
    }

    private void startGattService() {
        startForegroundService(new Intent(this, GattService.class));
    }

    private class GattServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof GattService.DataPlane) {
                gattServiceData = (GattService.DataPlane) service;
                gattServiceData.setMyCharacteristicChangedChannel(myCharacteristicValueChangeNotifications);
            } else {
                throw new IllegalArgumentException("Unknown service connected: " + name.getClassName());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            gattServiceData = null;
        }
    }
}