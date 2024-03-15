package com.example.healthkeeper.main;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.healthkeeper.databinding.FragmentAutoimgBinding;
import com.example.healthkeeper.databinding.RecvCommunityBoardBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Autoimg_Adapter extends FragmentStatePagerAdapter {
FragmentAutoimgBinding binding;
    private List<Integer> images = new ArrayList<>();

    public Autoimg_Adapter(@NonNull FragmentManager fm, List<Integer> images) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.images = images;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return AutoimgFragment.newInstance(Collections.singletonList(images.get(position)));
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
