package com.example.healthkeeper.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.databinding.FragmentHomeBinding;
import com.example.healthkeeper.member.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);


        binding.currentState.setOnClickListener(v -> {
            changeConditionFragment();
        });


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return binding.getRoot();


    }
    private void changeConditionFragment() {

        Intent intent = new Intent(getActivity(), ConditionActivity.class);
        startActivity(intent);
    }
}