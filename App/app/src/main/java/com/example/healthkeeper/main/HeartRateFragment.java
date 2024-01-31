package com.example.healthkeeper.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthkeeper.databinding.FragmentConditionBinding;
import com.example.healthkeeper.databinding.FragmentHeartRateBinding;

public class HeartRateFragment extends Fragment {

    FragmentHeartRateBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHeartRateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}