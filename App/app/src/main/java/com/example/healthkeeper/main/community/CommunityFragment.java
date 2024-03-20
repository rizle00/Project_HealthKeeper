package com.example.healthkeeper.main.community;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


import java.util.HashMap;
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
            Log.d("TAG", "aaaa: "+queList.size());
            Log.d("TAG", "aaaa: "+queList.get(0).getDto().getANSWER_CONTENT());

            binding.question.setAdapter(new Community_QuestionAdapter(inflater, queList, getContext()));
            binding.question.setLayoutManager((new LinearLayoutManager(getContext())));

            // queList에서 각 Community_QuestionDTO 객체의 id 값을 추출하여 리스트에 추가



//            createque4(result, inflater);//질문게시판
        });


      CommonConn conn2 = new CommonConn("faq/list");
        repository.select(conn2).thenAccept(result ->{
            faqList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_faqDTO>>() {
            }.getType());

            binding.faq.setAdapter(new Community_FaqAdapter(inflater,repository, faqList, getContext()));
            binding.faq.setLayoutManager((new LinearLayoutManager(getContext())));
        });


      CommonConn conn3 = new CommonConn("notice/list");
        repository.select(conn3).thenAccept(result ->{
            notiList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_NoticeDTO>>() {
            }.getType());

            binding.notice.setAdapter(new Community_NoticeAdapter(inflater,repository, notiList, getContext()));
            binding.notice.setLayoutManager((new LinearLayoutManager(getContext())));
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
                // 입력 폼을 보이도록 설정
                binding.tvNewWritingShow.setVisibility(View.VISIBLE);
                // 입력 폼을 초기화
                binding.edtWriterTltle.setText("");
                binding.edtWriterContent.setText("");

                // "글 저장" 버튼에 대한 클릭 리스너 설정
                binding.saveNewWrite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 사용자가 입력한 질문 제목과 내용 가져오기
                        String title = binding.edtWriterTltle.getText().toString().trim();
                        String content = binding.edtWriterContent.getText().toString().trim();

                        // 입력값이 유효한지 확인
                        if (title.isEmpty() || content.isEmpty()) {
                            // 제목 또는 내용이 입력되지 않은 경우 메시지 표시
                            Toast.makeText(getContext(), "제목과 내용을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                        } else {

                            SharedPreferences pref = requireActivity().getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
                            //회원이름 불러오기, 메인화면에 있는 정보 가져오게해야함.
                            String userName = pref.getString("user_name", "익명");//?????????????
                            // 서버로 전송할 데이터 맵 구성
                            HashMap<String, Object> params = new HashMap<>();
                            params.put("title", title);
                            params.put("content", content);
                            params.put("author", userName);



                            // 서버에 데이터 전송
                            CommonConn conn1_1 = new CommonConn("question/newWrite");
                            conn1_1.addParamMap("params", new Gson().toJson(params)); // 맵을 JSON 문자열로 변환하여 전송
                            repository.select(conn1).thenAccept(result -> {
                                queList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
                                    }.getType());
                                   Log.d("TAG", "size: "+queList.size());
                                   Log.d("TAG", "MEMBER_ID: "+queList.get(0).getMEMBER_ID());
                                   Log.d("TAG", "TITLE: "+queList.get(0).getTITLE());
                                   Log.d("TAG", "CONTENTㅍ: "+queList.get(0).getCONTENT());
                                binding.question.setAdapter(new Community_QuestionAdapter(inflater, queList, getContext()));
                                binding.question.setLayoutManager((new LinearLayoutManager(getContext())));



                                if (result != null) {
                                    // 성공적으로 데이터가 전송되었을 때의 처리
                                    Toast.makeText(getContext(), "글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // 전송 실패 시의 처리
                                    Toast.makeText(getContext(), "글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // 입력 폼 초기화
                            binding.edtWriterTltle.setText("");
                            binding.edtWriterContent.setText("");

                            // 입력 폼 숨기기
                            binding.tvNewWritingShow.setVisibility(View.GONE);
                            binding.tvNewWriting.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

//====================================================================================================
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




