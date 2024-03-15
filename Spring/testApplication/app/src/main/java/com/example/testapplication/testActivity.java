package com.example.testapplication;

import android.app.NotificationManager;
import android.content.Context;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.testapplication.databinding.ActivityTestBinding;

public class testActivity extends AppCompatActivity {

    ActivityTestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeFragment(new BlankFragment());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Button button = binding.button;
        button.setOnClickListener(view -> {
            notificationManager.cancel(getIntent().getIntExtra("id",0));
        });

    }
    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}