package com.example.healthkeeper.bt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.example.healthkeeper.R;

public class BtTest2 extends AppCompatActivity {

    private final String[] permissionArray = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ?
            new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            } :
            new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };

    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    for (String permission : permissions.keySet()) {
                        Log.d("DEBUG", permission + " = " + permissions.get(permission));
                    }
                });

//        setContent(() -> {
//            BtManager btManager = new BtManager(getApplicationContext());
//            NavHost navHost = new NavHost(
//                    rememberNavController(),
//                    "ScanScreen",
//                    builder -> {
//                        builder.composable("ScanScreen", () -> new ScanScreen(bleManager));
//                        builder.composable("ConnectScreen", () -> new ConnectScreen(bleManager));
//                    }
//            );
//
//            return new BleSampleTheme() {
//                Surface surface = new Surface(
//                        Modifier.fillMaxSize(),
//                        MaterialTheme.colorScheme().getBackground()
//                );
//                return surface.child(navHost);
//            };
//        });

        if (Build.VERSION.SDK_INT >= 31) {
            if (ContextCompat.checkSelfPermission(this, permissionArray[0]) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, permissionArray[1]) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, permissionArray[2]) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, permissionArray[3]) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한 확인", Toast.LENGTH_SHORT).show();
            } else {
                requestPermissionLauncher.launch(permissionArray);
            }
        }
    }
}