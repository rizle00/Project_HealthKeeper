package com.example.healthkeeper.main.monitor;

import android.content.Intent;
import android.graphics.Color;
import android.health.connect.datatypes.Record;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentDataBinding;
import com.example.healthkeeper.main.MainActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DataFragment extends Fragment {

    FragmentDataBinding binding;
    LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDataBinding.inflate(inflater, container, false);

        lineChart = binding.chart;

        binding.goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        binding.radioHeart.setOnClickListener(view -> {
            setHeartChart();
        });
        binding.radioTemp.setOnClickListener(view -> {
            setTempChart();
        });

        return binding.getRoot();
    }

    private void setHeartChart() {
        lineChart.invalidate();// 차트 초기화
        lineChart.clear(); //클리어

        ArrayList<Entry> hearts = new ArrayList<>(), temps = new ArrayList<>();

//        for (Record record : records) {// 데이터 담기
//            long dateTime = record.getDateTime();
//            float weight = (float) record.getWeight();
//            values.add(new Entry(dateTime, weight, hearts));
//        }

        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            hearts.add(new Entry(i, random.nextInt(201))); // 0부터 200까지의 랜덤한 값 생성
        }

        for (int i = 0; i < 40; i++) {
            temps.add(new Entry(i, random.nextFloat() * 5 + 34.5f)); // 34.5부터 39.5까지의 랜덤한 온도 값 생성
        }

        LineDataSet heartData = new LineDataSet(hearts, "심박"); //LineDataSet 선언
        heartData.setColor(ContextCompat.getColor(getContext(), R.color.pink)); //LineChart에서 Line Color 설정
        heartData.setCircleColor(ContextCompat.getColor(getContext(), R.color.pink)); // LineChart에서 Line Circle Color 설정
        heartData.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.pink)); // LineChart에서 Line Hole Circle Color 설정


        LineData lineData = new LineData(); //LineDataSet을 담는 그릇 여러개의 라인 데이터가 들어갈 수 있습니다.
        lineData.addDataSet(heartData);
        lineData.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black)); //라인 데이터의 텍스트 컬러 설정
        lineData.setValueTextSize(9);

        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setValueFormatter(new IndexAxisValueFormatter() ); //X축의 데이터를 제 가공함. new ChartXValueFormatter은 Custom한 소스
        xAxis.setLabelCount(30, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // X축 텍스트컬러설정
        xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.black)); // X축 줄의 컬러 설정

        YAxis yAxisLeft = lineChart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setLabelCount(6, true); // Y축 라벨 개수 설정
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        final String[] leftValues = {"0", "40", "80", "120", "160", "200"};
//        yAxisLeft.setValueFormatter(new IndexAxisValueFormatter(new String[]{"0", "40", "80", "120", "160", "200"})); // Y축 데이터 포매터 설정
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < leftValues.length) {
                    return leftValues[index];
                } else {
                    return "";
                }
            }
        });
        yAxisLeft.setAxisMaximum(200); // Y축 최대값 설정
        yAxisLeft.setAxisMinimum(0); // Y축 최소값 설정
        yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.pink)); //Y축 텍스트 컬러 설정
        yAxisLeft.setGridColor(ContextCompat.getColor(getContext(), R.color.pink)); // Y축 줄의 컬러 설정

        // Y축 줄의 컬러 설정
        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        lineChart.setVisibleXRangeMinimum(60 * 60 * 24 * 1000 * 5); //라인차트에서 최대로 보여질 X축의 데이터 설정
        lineChart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.
        Legend legend = lineChart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
//         legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);//하단 왼쪽에 설정
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // 레전드 컬러 설정
        lineChart.setData(lineData);

    }

    private void setTempChart() {
        lineChart.invalidate();// 차트 초기화
        lineChart.clear(); //클리어

        ArrayList<Entry> hearts = new ArrayList<>(), temps = new ArrayList<>();

//        for (Record record : records) {// 데이터 담기
//            long dateTime = record.getDateTime();
//            float weight = (float) record.getWeight();
//            values.add(new Entry(dateTime, weight, hearts));
//        }

        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            hearts.add(new Entry(i, random.nextInt(201))); // 0부터 200까지의 랜덤한 값 생성
        }

        for (int i = 0; i < 40; i++) {
            temps.add(new Entry(i, random.nextFloat() * 5 + 34.5f)); // 34.5부터 39.5까지의 랜덤한 온도 값 생성
        }


        LineDataSet tempData = new LineDataSet(temps, "체온"); //LineDataSet 선언
        tempData.setColor(ContextCompat.getColor(getContext(), R.color.black)); //LineChart에서 Line Color 설정
        tempData.setCircleColor(ContextCompat.getColor(getContext(), R.color.black)); // LineChart에서 Line Circle Color 설정
        tempData.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.black)); // LineChart에서 Line Hole Circle Color 설정


        LineData lineData = new LineData(); //LineDataSet을 담는 그릇 여러개의 라인 데이터가 들어갈 수 있습니다.
        lineData.addDataSet(tempData);
        lineData.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black)); //라인 데이터의 텍스트 컬러 설정
        lineData.setValueTextSize(9);

        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setValueFormatter(new IndexAxisValueFormatter() ); //X축의 데이터를 제 가공함. new ChartXValueFormatter은 Custom한 소스
        xAxis.setLabelCount(5, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // X축 텍스트컬러설정
        xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.black)); // X축 줄의 컬러 설정

        YAxis yAxisLeft = lineChart.getAxisLeft(); //Y축의 왼쪽면 설정
         // Y축 줄의 컬러 설정

        yAxisLeft.setLabelCount(7, true); // Y축 라벨 개수 설정
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        yAxisRight.setValueFormatter(new IndexAxisValueFormatter(new String[]{"0", "34.5", "35.5", "36.5", "37.5", "38.5", "39.5"})); // Y축 데이터 포매터 설정
        final String[] rightValues = {"0", "34.5", "35.5", "36.5", "37.5", "38.5", "39.5"};
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < rightValues.length) {
                    return rightValues[index];
                } else {
                    return "";
                }
            }
        });
        yAxisLeft.setAxisMaximum(39.5f); // Y축 최대값 설정
        yAxisLeft.setAxisMinimum(0); // Y축 최소값 설정
        yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); //Y축 텍스트 컬러 설정
        yAxisLeft.setGridColor(ContextCompat.getColor(getContext(), R.color.black)); // Y축 줄의 컬러 설정
// Y축 줄의 컬러 설정
        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        lineChart.setVisibleXRangeMinimum(60 * 60 * 24 * 1000 * 5); //라인차트에서 최대로 보여질 X축의 데이터 설정
        lineChart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.
        Legend legend = lineChart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
//         legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);//하단 왼쪽에 설정
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setTextColor(ContextCompat.getColor(getContext(), R.color.black)); // 레전드 컬러 설정
        lineChart.setData(lineData);

    }


    // LineChart.setData() 메서드 정의
    private void setData(LineChart chart, ArrayList<Entry> data, String label, String colorString) {
        int color = Color.parseColor(colorString);
        LineDataSet lineDataSet = new LineDataSet(data, label);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.white));
        LineData lineData = new LineData(lineDataSet);
        chart.setData(lineData);
    }

    private void initializeChart(LineChart chart) {
        int colorGrid = Color.parseColor("#CCCCCC");
        int colorText = Color.parseColor("#666666");

        chart.invalidate();
        chart.clear();
        chart.getDescription().setEnabled(false);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int time = (int) value;
                return String.format("%02d:%02d:%02d", time / 10000, (time % 10000) / 100, time % 100);
            }
        });
        chart.getXAxis().setLabelCount(CHART_SIZE, true);
        chart.getXAxis().setTextColor(colorText);
        chart.getXAxis().setGridColor(colorGrid);

        chart.getAxisLeft().setTextColor(colorText);
        chart.getAxisLeft().setGridColor(colorGrid);

        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);

        chart.getLegend().setTextColor(colorText);
    }
}

