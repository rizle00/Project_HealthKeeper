package com.example.testapplication;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.testapplication.common.CommonRepository;
import com.example.testapplication.databinding.FragmentBlankBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class BlankFragment extends Fragment {

   FragmentBlankBinding binding;
    CommonRepository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBlankBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        repository = new CommonRepository(((App) requireActivity().getApplication()).executorService, getContext());
        HashMap<String, Object> map = new HashMap<>();
        map.put("key","asd");


        repository.selectData("question/list",map).thenAccept(result->{
            Log.d("TAG", "onCreateView: "+result);
            createQues(result, inflater);
        });


        return view;
    }

    private void createQues(String result, LayoutInflater inflater) {
        // JSON 문자열을 파싱하여 리스트로 변환
        List<CommunityDTOS.Community_QuestionDTO> list = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>(){}.getType());
        binding.question.setAdapter(new Community_NoticeAdapter(inflater, list, getContext()));
        binding.question.setLayoutManager((new LinearLayoutManager(getContext())));

        Log.d("TAG", "createQues: "+list.get(0).getCONTENT());
    }
}