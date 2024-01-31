package com.example.healthkeeper.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.databinding.ActivityMainBinding;
import com.example.healthkeeper.databinding.FragmentHomeBinding;
import com.example.healthkeeper.main.ConditionActivity;



public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);


        binding.currentState.setOnClickListener(v -> {
            changeConditionFragment();
        });

        return binding.getRoot();
    }

    private void changeConditionFragment() {

        Intent intent = new Intent(getActivity(), ConditionActivity.class);
        startActivity(intent);
    }
}