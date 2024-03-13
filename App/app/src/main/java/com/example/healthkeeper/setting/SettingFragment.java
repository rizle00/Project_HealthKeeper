package com.example.healthkeeper.setting;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.databinding.FragmentSettingBinding;
import com.example.healthkeeper.member.PopupResiterActivity;


public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(getLayoutInflater());

        binding.llMemberModify.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ModifyActivity.class);
            startActivity(intent);
        });

        binding.llMemberRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PopupResiterActivity.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }

}