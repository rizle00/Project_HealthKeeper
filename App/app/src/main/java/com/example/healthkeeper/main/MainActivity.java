package com.example.healthkeeper.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.setting.MyMenuFragment;

public class MainActivity extends AppCompatActivity {
    /*다른 액티비티에서 MainActivity 종료를 위한 선언*/
    public static MainActivity _mainActivity;
    private AlertDialog alertDialog;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        changeFragment(new HomeFragment());

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {//nav 클릭시 화면선택!
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                changeFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_119) {
                showEmergencyDialog();
                return true;
            } else if (itemId == R.id.nav_commu) {

                return true;
            } else if (itemId == R.id.nav_schedule) {

                return true;
            } else if (itemId == R.id.nav_setting) {
                changeFragment(new MyMenuFragment());
                return true;
            }

            return false;
        });


    }
    public void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container , fragment).commit();
    }


    //긴급전화 다이얼로그처리
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

        confirmButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent= new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:긴급전화번호"));
                startActivity(callIntent);

                 alertDialog.dismiss();
            }
        }));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }



}