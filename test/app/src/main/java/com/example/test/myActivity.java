//package com.example.test;
//
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.content.pm.PackageManager;
//import android.os.*;
//import android.widget.TextView;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import kotlinx.coroutines.CoroutineScope;
//import kotlinx.coroutines.channels.Channel;
//import no.nordicsemi.android.support.v18.scanner.BuildConfig;
//
//public class myActivity extends AppCompatActivity {
//    private final CoroutineScope defaultScope = new CoroutineScope(Dispatchers.Default);
//    private final Handler mainHandler = new Handler(Looper.getMainLooper());
//    private GattServiceConn gattServiceConn;
//    private GattService.DataPlane gattServiceData;
//    private final Channel<String> myCharacteristicValueChangeNotifications = new Channel<>();
//
//    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), states -> {
//                if (states.get(Manifest.permission.BLUETOOTH_CONNECT) != null && states.get(Manifest.permission.ACCESS_FINE_LOCATION) != null) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        startForegroundService(new Intent(MainActivity.this, GattService.class));
//                    }
//                } else {
//                    finish();
//                }
//            });
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my);
//        TextView gattCharacteristicValue = findViewById(R.id.text);
//
//        defaultScope.launch(() -> {
//            for (String newValue : myCharacteristicValueChangeNotifications) {
//                mainHandler.post(() -> gattCharacteristicValue.setText(newValue));
//            }
//        });
//
//        String[] permissions;
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
//            permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
//        } else {
//            permissions = new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN};
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(new Intent(this, GattService.class));
//            }
//        } else {
//            requestPermissionLauncher.launch(permissions);
//        }
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        gattServiceConn = new GattServiceConn();
//        if (bindService(new Intent(GattService.DATA_PLANE_ACTION, null, this, GattService.class), gattServiceConn, 0)) {
//            gattServiceConn = gattServiceConn;
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (gattServiceConn != null) {
//            unbindService(gattServiceConn);
//            gattServiceConn = null;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        stopService(new Intent(this, GattService.class));
//    }
//
//    private class GattServiceConn implements ServiceConnection {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            if (BuildConfig.DEBUG && GattService.class.getName() != name.getClassName()) {
//                throw new RuntimeException("Connected to unknown service");
//            } else {
//                gattServiceData = (GattService.DataPlane) service;
//
//                gattServiceData.setMyCharacteristicChangedChannel(myCharacteristicValueChangeNotifications);
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            if (BuildConfig.DEBUG && GattService.class.getName() != name.getClassName()) {
//                throw new RuntimeException("Disconnected from unknown service");
//            } else {
//                gattServiceData = null;
//            }
//        }
//    }
//}