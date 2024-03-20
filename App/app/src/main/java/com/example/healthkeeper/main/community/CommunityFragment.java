package com.example.healthkeeper.main.community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.healthkeeper.App;
import com.example.healthkeeper.R;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.FragmentCommunityBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


import java.util.List;


public class CommunityFragment extends Fragment {
    FragmentCommunityBinding binding;

    List<CommunityDTOS.Community_QuestionDTO> queList;
    List<CommunityDTOS.Community_NoticeDTO> notiList;
    List<CommunityDTOS.Community_faqDTO> faqList;
    private CommonConn conn;

    CommonRepository repository;//스프링과 연결...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        repository = new CommonRepository(((App) requireActivity().getApplication()).executorService);
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("key", "test");



        //=============================================================
       CommonConn conn1 = new CommonConn("question/list");
       conn1.addParamMap("params", 5);
        repository.select(conn1).thenAccept(result -> {

            queList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
            }.getType());
            Log.d("TAG", "qqqq: "+queList.size());
            Log.d("TAG", "qqqq: "+result);

            binding.question.setAdapter(new Community_QuestionAdapter(inflater, queList, getContext()));
            binding.question.setLayoutManager((new LinearLayoutManager(getContext())));

            // queList에서 각 Community_QuestionDTO 객체의 id 값을 추출하여 리스트에 추가



//            createque4(result, inflater);//질문게시판
        });
      CommonConn conn2 = new CommonConn("faq/list");
        repository.select(conn2).thenAccept(result ->{
            faqList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_faqDTO>>() {
            }.getType());

            binding.question.setAdapter(new Community_FaqAdapter(inflater,repository, faqList, getContext()));
            binding.question.setLayoutManager((new LinearLayoutManager(getContext())));
        });


      CommonConn conn3 = new CommonConn("notice/list");
        repository.select(conn3).thenAccept(result ->{
            notiList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_NoticeDTO>>() {
            }.getType());

            binding.question.setAdapter(new Community_NoticeAdapter(inflater,repository, notiList, getContext()));
            binding.question.setLayoutManager((new LinearLayoutManager(getContext())));
        });






        binding.clickButton.setOnClickListener(new View.OnClickListener() {//read more를 누를시 전체 목록 나오게이동
            @Override
            public void onClick(View view) {

                questionDetail_Fragment();
            }

            private void questionDetail_Fragment() {
                QuestionDetail_Fragment QuestionDetailFragment = new QuestionDetail_Fragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, QuestionDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });



        binding.tvNewWriting.setOnClickListener(new View.OnClickListener() {//글쓰기 버튼
            @Override
            public void onClick(View view) {
                binding.tvNewWritingShow.setVisibility(View.VISIBLE);
                binding.tvNewWriting.setVisibility(View.INVISIBLE);
                

                
                
            }
        });



        binding.saveNewWrite.setOnClickListener(new View.OnClickListener() {//쓴글 저장.저장된 정보를 db와..연결?????????????해야함.???
            @Override
            public void onClick(View view) {

                binding.tvNewWritingShow.setVisibility(View.GONE);
                binding.tvNewWriting.setVisibility(View.VISIBLE);
            }
        });

        binding.button1.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.pink));//버튼 기본색상 변경
        binding.button2.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
        binding.button3.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));


        binding.button1.setOnClickListener(new View.OnClickListener() {
            // 게시판 버튼 클릭 시 게시판 RecyclerView로 스크롤
            @Override
            public void onClick(View view) {
                // 선택된 버튼 색상 설정
                binding.button1.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.pink));
                binding.button2.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
                binding.button3.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
                // NestedScrollView를 해당 버튼의 위치로 스크롤
                binding.scrollView.smoothScrollTo(0, binding.view1.getTop());
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            // 자주묻는 질문 버튼 클릭 시 질문 RecyclerView로 스크롤
            @Override
            public void onClick(View view) {
                // 선택된 버튼 색상 설정
                binding.button1.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
                binding.button2.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.pink));
                binding.button3.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));

                // NestedScrollView를 해당 버튼의 위치로 스크롤
                binding.scrollView.smoothScrollTo(0, binding.view2.getTop());
            }
        });

        binding.button3.setOnClickListener(new View.OnClickListener() {
            // 공지사항 버튼 클릭 시 공지사항 RecyclerView로 스크롤
            @Override
            public void onClick(View view) { // 선택된 버튼 색상 설정
                binding.button1.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
                binding.button2.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
                binding.button3.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.pink));

                // NestedScrollView를 해당 버튼의 위치로 스크롤
                binding.scrollView.smoothScrollTo(0, binding.view3.getTop());
            }
        });

        return view;
    }

    private void createque4(String result, LayoutInflater inflater) {
        // JSON 문자열을 파싱하여 리스트로 변환
        List<CommunityDTOS.Community_QuestionDTO> questionList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
        }.getType());


        Log.d("TAG", "createQues4: "+ questionList.size());
        // RecyclerView에 어댑터 설정


    }


    //private void createAnswer(String result, LayoutInflater inflater) {//질문게시판
        // JSON 문자열을 파싱하여 리스트로 변환
//        List<CommunityDTOS.Community_QuestionDTO> questionList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
//        }.getType());
//        List<CommunityDTOS.AnswerDTO> answerList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
//        }.getType());

//        Log.d("TAG", "createQues4: "+ questionList.size());
//        // RecyclerView에 어댑터 설정
//        binding.question.setAdapter(new Community_QuestionAdapter(inflater,answerList, questionList, getContext()));
//        binding.question.setLayoutManager((new LinearLayoutManager(getContext())));
    }

//
//    public void createFaq(String result, LayoutInflater inflater) {//자주묻는게시판
//        // JSON 문자열을 파싱하여 리스트로 변환
//        List<CommunityDTOS.Community_faqDTO> list = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_faqDTO>>() {
//        }.getType());
//
//        Log.d("TAG", "createFaq: "+list.get(0));
//        // RecyclerView에 어댑터 설정
//        Community_FaqAdapter recentBoardAdapter = new Community_FaqAdapter(inflater, repository, list, getContext());
//        binding.faq.setAdapter(recentBoardAdapter);
//        binding.faq.setLayoutManager(new LinearLayoutManager(getContext()));
//    }
//
//    private void createNotice(String result, LayoutInflater inflater) {//공지사항
//        // JSON 문자열을 파싱하여 리스트로 변환
//        List<CommunityDTOS.Community_NoticeDTO> list = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_NoticeDTO>>() {
//        }.getType());
//        // RecyclerView에 어댑터 설정
//        binding.notice.setAdapter(new Community_NoticeAdapter(inflater, repository, list, getContext()));
//        binding.notice.setLayoutManager((new LinearLayoutManager(getContext())));
//
//
//    }




