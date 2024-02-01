package com.example.healthkeeper.main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.home.HomeFragment;
import com.example.healthkeeper.member.LoginActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0f);
            actionBar.setTitle("healthkeeper");
        }

        changeFragment(new HomeFragment());


        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                changeFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.nav_119) {

            } else if (item.getItemId() == R.id.nav_commu) {

            } else if (item.getItemId() == R.id.nav_schedule) {

            } else if (item.getItemId() == R.id.nav_setting) {

            }
            actionBar.setTitle(item.getTitle());

            return true;

        });
    }


    public void changeFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}