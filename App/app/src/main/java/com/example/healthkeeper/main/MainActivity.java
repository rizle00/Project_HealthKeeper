package com.example.healthkeeper.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    /*다른 액티비티에서 MainActivity 종료를 위한 선언*/
    public static MainActivity _mainActivity;

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View childview=getLayoutInflater().inflate(R.layout.fragment_home,null);
        binding.container.addView(childview);


        changeFragment(new HomeFragment());

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {//nav 클릭시 화면선택!
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                changeFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_119) {

                return true;
            } else if (itemId == R.id.nav_commu) {

                return true;
            } else if (itemId == R.id.nav_schedule) {

                return true;
            } else if (itemId == R.id.nav_setting) {

                return true;
            }

            return false;
        });


    }
    public void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container , fragment).commit();
    }

}