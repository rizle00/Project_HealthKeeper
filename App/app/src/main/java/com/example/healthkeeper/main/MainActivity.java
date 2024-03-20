package com.example.healthkeeper.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.*;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.bluetooth.BluetoothConnector;
import com.example.healthkeeper.bluetooth.BluetoothService;
import com.example.healthkeeper.bluetooth.BluetoothViewModel;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.main.community.CommunityActivity;
import com.example.healthkeeper.setting.SettingFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    //블루투스 관련 ---------------
    private static final int INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701;
    private BluetoothService bluetoothService;
    private boolean sBound, active;// gatt 서비스, bluetooth 서비스 연결 체크
    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothConnector btConnector;
    private MenuItem btItem;

    private BluetoothViewModel viewModel;
    //------------------------------------
    private AlertDialog alertDialog;
    private ActivityMainBinding binding;

    private final PermissionUtils permission = new PermissionUtils();

    private boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        active = isForegroundServiceRunning(MainActivity.this, 2000);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int customTextColor = getResources().getColor(R.color.white);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//툴바에 뒤로가기 아이콘 생성

        getSupportActionBar().setTitle(getColoredSpanned("홈", customTextColor));

        changeFragment(new HomeFragment());//화면에 처음 보여질 fragment설정!!

        
        bottomNavSetting();// 메뉴 선택 리스너
        permission.checkPermission(permissionListener);


    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    private void bottomNavSetting() {
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int customTextColorNav = getResources().getColor(R.color.white);

            if (itemId == R.id.nav_home) {
                changeFragment(new HomeFragment());
                getSupportActionBar().setTitle(getColoredSpanned("홈", customTextColorNav));
                setToolbarMenu(R.menu.toolbar_home);
                return true;
            } else if (itemId == R.id.nav_119) {
                permission.checkCallPermission(callPermissionListener);

                return true;
            } else if (itemId == R.id.nav_commu) {
                Intent intent = new Intent(this, CommunityActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_schedule) {
                changeFragment(new ScheduleFragment());
                getSupportActionBar().setTitle(getColoredSpanned("일정관리", customTextColorNav));
                setToolbarMenu(R.menu.toolbar_schedule);
                return true;
            } else if (itemId == R.id.nav_setting) {
                changeFragment(new SettingFragment());
                getSupportActionBar().setTitle(getColoredSpanned("나의 메뉴", customTextColorNav));
                setToolbarMenu(R.menu.toolbar_setting);
                return true;
            }

            return false;
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//툴바메뉴 붙이기
        int selectedItem = binding.bottomNav.getSelectedItemId();
        if (selectedItem == R.id.nav_home) {
            getMenuInflater().inflate(R.menu.toolbar_home, menu);

            // 포그라운드 서비스 작동중일시, 블루투스버튼
            if (active) {

                menu.getItem(1).setIcon(R.drawable.baseline_bluetooth_conn);
                if(!sBound){// 서비스 바인드 풀렸을시
                    Intent intent = new Intent(MainActivity.this, BluetoothService.class);
                    bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

                }
            } else {
                menu.getItem(1).setIcon(R.drawable.baseline_bluetooth_no_conn);
            }
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//toolbar 메뉴 선택시 !!
        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            SharedPreferences pref = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            Intent intent = new Intent(this, LoginBeforeActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.menu_alam) {
            // 처리 추가
            return true;
        } else if (id == R.id.menu_bluetooth && !active) {
            btItem = item;
            // 블루투스 버튼 클릭 했는데 서비스 x
            if(!sBound){
                if (isOn()) { // 블루투스 사용 가능한 경우
                    permission.checkBtPermission(bTPermissionListener);
                } else { // 블루투스 활성화가 필요한 경우
                    requestBluetoothActivation(MainActivity.this);
                }
            }
            return true;
        }
        else if (id == R.id.menu_bluetooth && active) {
            // 블루투스 버튼 클릭 했는데 서비스 o

            bluetoothService.stopService();
            active = bluetoothService.getActive();
            if (sBound) {
                unbindService(mServiceConnection);
                sBound = false; // 바인드 상태 갱신
            }
            item.setIcon(R.drawable.baseline_bluetooth_no_conn);
            return true;
        }else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private SpannableString getColoredSpanned(String text, int color) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showEmergencyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_emergency, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        dialogTitle.setText("긴급 전화");
        dialogMessage.setText("긴급 전화 연결됩니다.\n 연결하시겠습니까?");

        confirmButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:긴급전화번호"));
            startActivity(callIntent);

            alertDialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void setToolbarMenu(int menuResId) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        getMenuInflater().inflate(menuResId, toolbar.getMenu());
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "한번 더 뒤로가기를 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    //포그라운드 서비스 작동중 체크
    public boolean isForegroundServiceRunning(Context context, int notificationId) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = manager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == notificationId) {
                return true;
            }
        }
        return false;
    }

    // 블루투스 활성 체크
    private boolean isOn() {
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

    // 블루투스 활성 확인 시 권한체크, 취소 시 알림
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_BLUETOOTH_ENABLE && resultCode == RESULT_OK) {
            permission.checkBtPermission(bTPermissionListener);
        } else if (requestCode == INTENT_REQUEST_BLUETOOTH_ENABLE && resultCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "블루투스 활성화가 필요합니다", Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private final PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 권한 설정됨, 긴급전화 시행
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //권한 거부
            makeDenyDialog("noti");

        }
    };
    private final PermissionListener callPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 권한 설정됨, 긴급전화 시행
            showEmergencyDialog();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //권한 거부
            makeDenyDialog("call");

        }
    };
    //  권한 체크 리스너 등록 -블루투스
    private final PermissionListener bTPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 권한 설정됨, 블루투스 초기화 시행
            Intent intent = new Intent(MainActivity.this, BluetoothService.class);
            bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //권한 거부
            makeDenyDialog("bt");

        }
    };

    private void makeDenyDialog(String name) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("권한 요청")
                .setMessage("권한이 반드시 필요합니다.!!미허용시 기능 사용 불가!")
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "권한 허용이 필요합니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(name.equals("bt")){
                                    permission.checkBtPermission(bTPermissionListener);
                                } else if (name.equals("call")){
                                    permission.checkCallPermission(callPermissionListener);
                                }else if (name.equals("noti")){
                                    permission.checkPermission(permissionListener);
                                }
                            }
                        })

                .setCancelable(true)
                .show();
    }

    private void observeData() {

        if (bluetoothService != null) {
            btConnector = bluetoothService.getmBtConnector();

            Log.d(TAG, "observeData: "+btConnector);
            if (btConnector != null) {
                viewModel.getBtLiveData().observe(MainActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String  data) {
                        Log.d(TAG, "onChanged: "+data);
                        if("off".equals(data)){
                            Toast.makeText(MainActivity.this, "기기 연결이 해제되었습니다", Toast.LENGTH_SHORT).show();
                        } else if ("on".equals(data)) {
                            Toast.makeText(MainActivity.this, "기기가 연결되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetoothService =
                    ((BluetoothService.LocalBinder) service).getService();
            sBound = true;
            bluetoothService.setContext(MainActivity.this);
            Log.d(TAG, "onServiceConnected: "+active);
            if(!active){// 앱 처음 시작시 포그라운드 서비스를 실행하지 않았을때 포그라운드가 시행중이면 다시 시작x
                ContextCompat.startForegroundService(MainActivity.this,
                        new Intent(MainActivity.this, BluetoothService.class));
                active = bluetoothService.getActive();
            }

            // 서비스가 연결되어 있는 경우 스위치를 켜기
            if(btItem != null){

            btItem.setIcon(R.drawable.baseline_bluetooth_conn);
            }

            Log.d(TAG, "onServiceConnected: "+sBound);
            viewModel = ((App) getApplicationContext()).getSharedViewModel();
            observeData();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
//            bluetoothService = null;
            Log.d(TAG, "onServiceDisconnected: "+bluetoothService.getActive());
            sBound = false;

        }
    };
}
