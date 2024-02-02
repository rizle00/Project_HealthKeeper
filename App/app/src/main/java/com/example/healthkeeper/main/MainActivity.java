package com.example.healthkeeper.main;

import android.os.Bundle;
import android.view.Menu;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // 초기 프래그먼트 설정
        changeFragment(new HomeFragment());

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    changeFragment(new HomeFragment(), "Home");
                    return true;
                case R.id.nav_119:
                    // 처리 추가
                    return true;
                case R.id.nav_commu:
                    // 처리 추가
                    return true;
                case R.id.nav_schedule:
                    // 처리 추가
                    return true;
                case R.id.nav_setting:
                    // 처리 추가
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav_btm, menu);
        return true;
    }

    private void changeFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
