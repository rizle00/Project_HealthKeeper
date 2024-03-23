package com.example.healthkeeper.main.monitor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentDataBinding;
import com.example.healthkeeper.main.MainActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class DataFragment extends Fragment {

    FragmentDataBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDataBinding.inflate(inflater, container, false);


        binding.goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });


        return binding.getRoot();
    }

    private void setBarChart() {
        binding.chart.apply

        // data set
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(14f, "Apple"));
        entries.add(new PieEntry(22f, "Orange"));
        entries.add(new PieEntry(7f, "Mango"));
        entries.add(new PieEntry(31f, "RedOrange"));
        entries.add(new PieEntry(26f, "Other"));

        // add a lot of colors
        ArrayList<Integer> colorsItems = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS) colorsItems.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS) colorsItems.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS) colorsItems.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS) colorsItems.add(c);
        colorsItems.add(ColorTemplate.getHoloBlue());

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(colorsItems);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(18f);

        PieData pieData = new PieData(pieDataSet);
        binding.pieChart.setData(pieData);
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.setRotationEnabled(false);
        binding.pieChart.setCenterText("진단 결과");
        binding.pieChart.setEntryLabelColor(Color.BLACK);
        binding.pieChart.setCenterTextSize(20f);
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad);
        binding.pieChart.animate();
    }
}