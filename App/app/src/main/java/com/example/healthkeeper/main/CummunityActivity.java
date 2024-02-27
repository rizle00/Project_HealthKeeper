package com.example.healthkeeper.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityCommunityBinding;

public class CummunityActivity extends AppCompatActivity {
    ActivityCommunityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(CummunityActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_119) {
               // showEmergencyDialog();
                return true;
            }
            return false;
        });

    }

    /*private void showEmergencyDialog() {//긴급전화 다이얼로그
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
                callIntent.setData(Uri.parse("tel:긴급전화번호"));
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


}

*/
}