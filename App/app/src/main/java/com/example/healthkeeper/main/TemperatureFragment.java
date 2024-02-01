package com.example.healthkeeper.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.healthkeeper.databinding.FragmentHeartRateBinding;
import com.example.healthkeeper.databinding.FragmentTemperatureBinding;

public class TemperatureFragment extends Fragment {


    FragmentTemperatureBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTemperatureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
