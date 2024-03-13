package com.example.healthkeeper.main.community;

        import static android.app.PendingIntent.getActivity;

        import android.Manifest;
        import android.content.DialogInterface;
        import android.content.SharedPreferences;
        import android.widget.Toast;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.text.Spannable;
        import android.text.SpannableString;
        import android.text.style.ForegroundColorSpan;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.appcompat.widget.Toolbar;
        import androidx.fragment.app.Fragment;

        import com.example.healthkeeper.R;
        import com.example.healthkeeper.databinding.ActivityCommunityBinding;
        import com.example.healthkeeper.main.cctv.CCTVActivity;
        import com.example.healthkeeper.main.monitor.ConditionActivity;
        import com.example.healthkeeper.main.LoginBeforeActivity;
        import com.example.healthkeeper.main.MainActivity;
        import com.example.healthkeeper.setting.SettingFragment;
        import com.gun0912.tedpermission.PermissionListener;
        import com.gun0912.tedpermission.normal.TedPermission;

        import java.util.List;

public class CommunityActivity extends AppCompatActivity {
    ActivityCommunityBinding binding;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        changeFragment(new CommunityFragment());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int customTextColor = getResources().getColor(R.color.white);
        getSupportActionBar().setTitle(getColoredSpanned("COMMUNITY", customTextColor));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                  //부모 액티비티로의 이동이나 앱의 계층 구조 내에서 위로 이동하는 데 사용

        binding.bottomNav.setOnItemSelectedListener(item -> {//btm_menu 클릭시 설정
            int itemId = item.getItemId();


            if (itemId == R.id.btm_home) {
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.btm_emergency) {
               checkCallPermission();// 권한 확인 필요
                return true;
            } else if (itemId == R.id.btm_setting) {
                changeFragment(new SettingFragment());
                getSupportActionBar().setTitle("");
                return true;
            }
            return false;
        });

    }

    private SpannableString getColoredSpanned(String text, int color) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//툴바의 메뉴생성
        getMenuInflater().inflate(R.menu.toolbar_community, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){//툴바 메뉴 눌렀을때 전환
        int id= item.getItemId();
        if(id==R.id.menu_camera){
            Intent intent=new Intent(this, CCTVActivity.class);
            startActivity(intent);
            finish();
            return true;
        }else if(id==R.id.menu_condition){
            Intent intent= new Intent(this, ConditionActivity.class);
            startActivity(intent);
            finish();


            return true;
        }else if(id==R.id.menu_logout){
            SharedPreferences pref = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();// 로그아웃시 피워야함
            Intent intent=new Intent(this, LoginBeforeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }else if (id == android.R.id.home) { //toolbar의 back키 눌렀을 때 동작
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // 권한 체크 - 전화
    private void checkCallPermission() {
        TedPermission.create()
                .setPermissionListener(callPermissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(
                        Manifest.permission.CALL_PHONE
                )

                .check();
    }
    private final PermissionListener callPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 권한 설정됨, 긴급전화 시행
            showEmergencyDialog();
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
                .setMessage("권한이 반드시 필요합니다.!!미허용시 기능 사용 불가!")
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CommunityActivity.this, "권한 허용이 필요합니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                    checkCallPermission();

                            }
                        })

                .setCancelable(true)
                .show();
    }


    private void showEmergencyDialog() {//긴급전화 다이얼로그
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_emergency, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        dialogTitle.setText("긴급 전화");
        dialogMessage.setText("긴급 전화 연결됩니다.\n 연결하시겠습니까?");

        confirmButton.setOnClickListener((new View.OnClickListener() {//긴급전화 확인버튼
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:119"));
                startActivity(callIntent);

                alertDialog.dismiss();
            }
        }));

        cancelButton.setOnClickListener(new View.OnClickListener() {//긴급전화 취소버튼
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼 눌렀을 때의 동작 구현
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}


