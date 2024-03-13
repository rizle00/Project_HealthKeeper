package com.example.healthkeeper.main.monitor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentDataBinding;


public class DataFragment extends Fragment {

  FragmentDataBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentDataBinding.inflate( inflater, container, false);
        return binding.getRoot();
    }
}