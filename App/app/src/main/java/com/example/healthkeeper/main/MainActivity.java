package com.example.healthkeeper.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.example.healthkeeper.R;
import com.example.healthkeeper.bt.BTtestActivity;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.home.HomeFragment;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        changeFragment(new HomeFragment());


    }
    public void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container , fragment).commit();
    }

}