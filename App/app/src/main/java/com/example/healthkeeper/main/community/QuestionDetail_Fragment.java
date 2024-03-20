package com.example.healthkeeper.main.community;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.App;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;

import com.example.healthkeeper.databinding.FragmentQuestionDetailBinding;
import com.example.healthkeeper.main.MainActivity;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;


public class QuestionDetail_Fragment extends Fragment {
    private static final String TAG = QuestionDetail_Fragment.class.getSimpleName();
    FragmentQuestionDetailBinding binding;
    private List<CommunityDTOS.Community_QuestionDTO> questionList;
    private List<CommunityDTOS.AnswerVO> answerList;
    CommonRepository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
               binding=FragmentQuestionDetailBinding.inflate(inflater, container, false);
                View view=binding.getRoot();

        repository = new CommonRepository(((App) requireActivity().getApplication()).executorService);

        CommonConn conn = new CommonConn("question/list");
//        conn.addParamMap("params","");
        repository.select(conn).thenAccept(result -> {
            questionList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
            }.getType());
            Log.d(TAG, "onCreateView: "+questionList.size());
            binding.questionDetailList.setAdapter(new Community_QuestionAdapter(inflater, questionList, getContext()));
            binding.questionDetailList.setLayoutManager((new LinearLayoutManager(getContext())));
            // queList에서 각 Community_QuestionDTO 객체의 id 값을 추출하여 리스트에 추가



//            createque4(result, inflater);//질문게시판
        });





        binding.tvNewWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvNewWritingShow.setVisibility(View.VISIBLE);
                binding.tvNewWriting.setVisibility(View.INVISIBLE);
            }
        });
        binding.saveNewWrite.setOnClickListener(new View.OnClickListener() {//저장된 정보를 db와..연결?????????????해야함.???
            @Override
            public void onClick(View view) {

                binding.tvNewWritingShow.setVisibility(View.GONE);
                binding.tvNewWriting.setVisibility(View.VISIBLE);

            }
        });


                binding.goHome.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               MainActivity();
                            }
                        });

                binding.goCommunity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getParentFragmentManager().popBackStack();//이전화면(CommunityActivity)으로 이동

                    }
                });


        return view;
    }

    private void createQues(String result, LayoutInflater inflater) {
        // JSON 문자열을 파싱하여 리스트로 변환
        List<CommunityDTOS.Community_QuestionDTO> list = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
        }.getType());
        // RecyclerView에 어댑터 설정
//        binding.questionDetailList.setAdapter(new Community_QuestionAdapter(inflater,answerList, list, getContext()));
//        binding.questionDetailList.setLayoutManager((new LinearLayoutManager(getContext())));
    }

   /* public void createAllBoard(String result, LayoutInflater inflater) {
        // JSON 문자열을 파싱하여 리스트로 변환
        List<CommunityDTOS.Community_faqDTO> list = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_faqDTO>>(){}.getType());

        // RecyclerView에 어댑터 설정
        Community_FaqAdapter recentBoardAdapter = new Community_FaqAdapter(inflater, list, getContext());
        binding.boardDetailList.setAdapter(recentBoardAdapter);
        binding.boardDetailList.setLayoutManager(new LinearLayoutManager(getContext()));
    }*/







    private void MainActivity(){
        Intent intent=new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();

    }
    public List<CommunityDTOS.Community_QuestionDTO> getcreateque() {
        return questionList;

    }



}