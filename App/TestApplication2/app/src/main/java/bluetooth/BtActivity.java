package bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.*;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.testapplication2.BtService;
import com.example.testapplication2.MyService;
import com.example.testapplication2.R;
import com.example.testapplication2.TestActivity;
import com.example.testapplication2.databinding.ActivityTestBinding;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class BtActivity extends AppCompatActivity {

    private final static String TAG = TestActivity.class.getSimpleName();
    public static final int INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701;
    ActivityTestBinding binding;// 바인딩  처리
    private BluetoothService bluetoothService;
    private boolean mBound, sBound;// gatt 서비스 연결 체크
    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private int heart, accident;
    private double temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mBound = bluetoothService.getBound();
        btSwitch();
    }


    //  블루투스 커넥트 요청시 , 서비스의 온오프
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void btSwitch() {
        Switch toggleSwitch = binding.toggleSwitch;

        // 스위치 상태 변경 이벤트 리스너 설정
        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 상태 변경 시 호출되는 메서드
                if (isChecked) {

                    if (isOn()) {// 블루투스 사용 가능 체크 후 권한체크, 권한 허용시 서비스 연결 시도
                        checkPermission();

                    } else {//블루투스 기능 활성 요청
                        requestBluetoothActivation(BtActivity.this);
                    }
                } else {// 블루투스 해제, 서비스
                    unbindService(mServiceConnection);
                }
            }
        });
    }

    // 블루투스 활성 체크
    public boolean isOn() {
        return adapter.isEnabled();
    }

    // Bluetooth 활성화 요청
    @SuppressLint("MissingPermission")
    public void requestBluetoothActivation(AppCompatActivity activity) {
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, INTENT_REQUEST_BLUETOOTH_ENABLE);
        }
    }

    // 활성 확인 시 권한체크, 취소 시 알림
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_BLUETOOTH_ENABLE && resultCode == RESULT_OK) {
            checkPermission();
        } else if (requestCode == INTENT_REQUEST_BLUETOOTH_ENABLE && resultCode == RESULT_CANCELED) {
            Toast.makeText(BtActivity.this, "블루투스 활성화가 필요합니다", Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    // 권한체크
    private void checkPermission() {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
                .check();
    }

    //  권한 체크 리스너 등록
    private final PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 권한 설정됨, 블루투스 초기화 시행
            Intent intent = new Intent(BtActivity.this, BluetoothService.class);
            bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //권한 거부
            makeDenyDialog();

        }
    };

    private void makeDenyDialog() {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("권한 요청")
                .setMessage("권한이 반드시 필요합니다.!!미허용시 앱 사용 불가!")
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(BtActivity.this, "권한 허용이 필요합니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPermission();
                            }
                        })

                .setCancelable(true)
                .show();
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothService = ((BluetoothService.LocalBinder) service).getService();
            sBound = true;
            bluetoothService.getContext(BtActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetoothService = null;
            sBound = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}