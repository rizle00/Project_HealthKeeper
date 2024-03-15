package com.example.healthkeeper.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentAutoimgBinding;

import java.util.ArrayList;
import java.util.List;

public class AutoimgFragment extends Fragment {
    FragmentAutoimgBinding binding;

    private ViewPager viewPager;
    private Autoimg_Adapter adapter;

    public AutoimgFragment() {
        // Required empty public constructor
    }

    public static AutoimgFragment newInstance(List<Integer> images) {
        AutoimgFragment fragment = new AutoimgFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList("images", (ArrayList<Integer>) images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            List<Integer> images = getArguments().getIntegerArrayList("images");
            adapter = new Autoimg_Adapter(getChildFragmentManager(), images);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_autoimg, container, false);

        // Initialize ViewPager
        viewPager = view.findViewById(R.id.viewPager);

        // Set adapter to ViewPager
        if (adapter != null) {
            viewPager.setAdapter(adapter);
        }

        return view;
    }
}
