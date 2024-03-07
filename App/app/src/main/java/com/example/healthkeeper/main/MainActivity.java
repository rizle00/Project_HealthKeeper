package com.example.healthkeeper.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.healthkeeper.R;
import com.example.healthkeeper.bluetooth.BluetoothConnector;
import com.example.healthkeeper.bluetooth.BluetoothService;
import com.example.healthkeeper.bluetooth.BtActivity;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.setting.SettingFragment;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    //블루투스 관련 ---------------
    private static final int INTENT_REQUEST_BLUETOOTH_ENABLE = 0x0701;
    private BluetoothService bluetoothService;
    private boolean sBound, active;// gatt 서비스, bluetooth 서비스 연결 체크
    private final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothConnector btConnector;
    //------------------------------------
    private AlertDialog alertDialog;
    private ActivityMainBinding binding;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        active = isForegroundServiceRunning(this, 2000);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int customTextColor = getResources().getColor(R.color.white);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//툴바에 뒤로가기 아이콘 생성

        getSupportActionBar().setTitle(getColoredSpanned("홈", customTextColor));

        changeFragment(new HomeFragment());//화면에 처음 보여질 fragment설정!!

        bottomNavSetting();
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
                showEmergencyDialog();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        int selectedItem = binding.bottomNav.getSelectedItemId();
        if (selectedItem == R.id.nav_home) {
            getMenuInflater().inflate(R.menu.toolbar_home, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if  (id == R.id.menu_logout) {
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
        } else if (id == android.R.id.home) {
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

}
