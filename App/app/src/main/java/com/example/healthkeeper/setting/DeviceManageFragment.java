package com.example.healthkeeper.setting;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.healthkeeper.databinding.FragmentDeviceManageBinding;

public class DeviceManageFragment extends Fragment {
    FragmentDeviceManageBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeviceManageBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}