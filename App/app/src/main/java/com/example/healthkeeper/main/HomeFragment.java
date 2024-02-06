package com.example.healthkeeper.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.databinding.FragmentHomeBinding;
import com.example.healthkeeper.main.ConditionActivity;
import com.example.healthkeeper.main.MainActivity;
import com.example.healthkeeper.member.LoginActivity;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);


        binding.case1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ConditionActivity.class));
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LoginActivity.class));

            }
        });

        return binding.getRoot();


    }
    private void changeConditionFragment() {

        Intent intent = new Intent(getActivity(), ConditionActivity.class);
        startActivity(intent);
    }

}