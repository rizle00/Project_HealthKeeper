package com.example.healthkeeper.setting;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.databinding.FragmentMyMenuBinding;


public class MyMenuFragment extends Fragment {
    FragmentMyMenuBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyMenuBinding.inflate(getLayoutInflater());

        binding.llMemberModify.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ModifyGuardianActivity.class);
            startActivity(intent);
        });



        return binding.getRoot();
    }

}