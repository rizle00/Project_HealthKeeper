package com.example.healthkeeper.setting;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentMyMenuBinding;


public class MyMenuFragment extends Fragment {
    FragmentMyMenuBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_menu, container, false);
    }
}