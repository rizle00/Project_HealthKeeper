package com.example.healthkeeper.main.community;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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


import java.util.ArrayList;
import java.util.List;


public class CommunityFragment extends Fragment {
    FragmentCommunityBinding binding;
    private boolean isMoreFaqShown = false;//더 보기 버튼 클릭 상태를 나타내는 변수
    private boolean isMoreNoticeShown = false;//더 보기 버튼 클릭 상태를 나타내는 변수

    List<CommunityDTOS.Community_QuestionDTO> queList;
    List<CommunityDTOS.Community_NoticeDTO> notiList;
    private Community_FaqAdapter faqAdapter;
    private Community_NoticeAdapter noticeAdapter;
    List<CommunityDTOS.Community_faqDTO> faqList = new ArrayList<>();
    private Spinner spinner;
    CommonRepository repository;//스프링과 연결...
    private CommunityDTOS.Community_QuestionDTO vo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initRecyclerViewFaq(inflater);
        getInitialFaqList();
        initMoreButtonFaq();

//        initRecyclerViewNotice(inflater);
//        getInitialNoticeList();
//        initMoreButtonNotice();


        spinner = binding.spinnerCategory;
        repository = new CommonRepository(((App) requireActivity().getApplication()).executorService);
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("key", "test");


        //=============================================================
        createQuestion(inflater);


        CommonConn conn2 = new CommonConn("faq/list");
        repository.select(conn2).thenAccept(result -> {
            faqList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_faqDTO>>() {
            }.getType());

            binding.faq.setAdapter(new Community_FaqAdapter(inflater, faqList, getContext()));
            binding.faq.setLayoutManager((new LinearLayoutManager(getContext())));
        });


        CommonConn conn3 = new CommonConn("notice/list");
        repository.select(conn3).thenAccept(result -> {
            notiList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_NoticeDTO>>() {
            }.getType());

            binding.notice.setAdapter(new Community_NoticeAdapter(inflater, notiList, getContext()));
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
                vo = new CommunityDTOS.Community_QuestionDTO();
                // 입력 폼을 보이도록 설정
                binding.tvNewWritingShow.setVisibility(View.VISIBLE);
                // 입력 폼을 초기화
                binding.edtWriterTltle.setText("");
                binding.edtWriterContent.setText("");
                radioButton();
                CommonConn reqCategory = new CommonConn("category");
                repository.select(reqCategory).thenAccept(result -> {
                    List<CommunityDTOS.CategoryVO> list = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.CategoryVO>>() {
                    }.getType());
                    List<String> categoryNames = new ArrayList<>();
                    for (CommunityDTOS.CategoryVO category : list) {
                        categoryNames.add("카테고리");
                        categoryNames.add(category.getNAME());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selectedHospital = categoryNames.get(i);
                            vo.setCATEGORY_ID(list.get(i - 1).getCATEGORY_ID());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            vo.setCATEGORY_ID("12");
                        }
                    });
                });

                // "글 저장" 버튼에 대한 클릭 리스너 설정
                binding.saveNewWrite.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SuspiciousIndentation")
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
                            String id = pref.getString("user_id", "익명");//?????????????
                            // 서버로 전송할 데이터 맵 구성


                            vo.setTITLE(title);
                            vo.setCONTENT(content);
                            vo.setMEMBER_ID(id);


                            // 서버에 데이터 전송
                            CommonConn write = new CommonConn("question/newWrite");
                            write.addParamMap("params", new Gson().toJson(vo)); // 맵을 JSON 문자열로 변환하여 전송
                            repository.select(write).thenAccept(result -> {

                                if (result != null) {
                                    // 성공적으로 데이터가 전송되었을 때의 처리
                                    Toast.makeText(getContext(), "글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    createQuestion(inflater);
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
        initButton();

        return view;
    }


    private void initButton() {
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
    }

    private void radioButton() {
        // 비밀글 선택 시
        binding.radioButtonSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 값이 "y"가 되도록 설정
                String selectedValue = "y";
                // 여기서는 선택된 값을 어떻게 활용할지에 대한 코드를 추가할 수 있습니다.
                vo.setSECRET("y");
            }
        });

        // 공개글 선택 시
        binding.radioButtonPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 값이 "n"이 되도록 설정
                String selectedValue = "n";
                // 여기서는 선택된 값을 어떻게 활용할지에 대한 코드를 추가할 수 있습니다.
                vo.setSECRET("n");
            }
        });

    }

    private void createQuestion(LayoutInflater inflater) {
        CommonConn conn1 = new CommonConn("question/list");
        conn1.addParamMap("params", 5);
        repository.select(conn1).thenAccept(result -> {

            queList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
            }.getType());
            Log.d("TAG", "aaaa: " + queList.size());
            Log.d("TAG", "aaaa: " + queList.get(0).getDto().getANSWER_CONTENT());

            // RecyclerView 어댑터 설정을 여기서 수행
            Community_QuestionAdapter questionAdapter = new Community_QuestionAdapter(inflater, queList, getContext());

            binding.question.setAdapter(questionAdapter);
            binding.question.setLayoutManager((new LinearLayoutManager(getContext())));

            // queList에서 각 Community_QuestionDTO 객체의 id 값을 추출하여 리스트에 추가


//            createque4(result, inflater);//질문게시판
        });
    }


    private void createque4(String result, LayoutInflater inflater) {
        // JSON 문자열을 파싱하여 리스트로 변환
        List<CommunityDTOS.Community_QuestionDTO> questionList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
        }.getType());


        Log.d("TAG", "createQues4: " + questionList.size());
        // RecyclerView에 어댑터 설정


    }


    //==============Faq====================================================================

    private void initRecyclerViewFaq(LayoutInflater inflater) {
        faqAdapter = new Community_FaqAdapter(LayoutInflater.from(requireContext()), getInitialFaqList(), requireContext());
        binding.faq.setAdapter(faqAdapter);
        binding.faq.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void initMoreButtonFaq() {
        Button moreButton = binding.openFaq;
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMoreFaqShown) {
                    // "더 보기" 버튼이 클릭되었을 때
                    faqAdapter.addAll(getMoreFaqList()); // 추가 아이템 표시
                    binding.openFaq.setText("접기"); // 버튼 텍스트 변경
                } else {
                    // "접기" 버튼이 클릭되었을 때

                    binding.openFaq.setText("더 보기"); // 버튼 텍스트 변경
                    // 최초 상태로 돌아감
                    faqAdapter = new Community_FaqAdapter(LayoutInflater.from(requireContext()), getInitialFaqList(), requireContext());
                    binding.faq.setAdapter(faqAdapter);
                    binding.faq.setLayoutManager(new LinearLayoutManager(requireContext()));

                    binding.scrollView.smoothScrollTo(0, binding.view2.getTop());

                }
                isMoreFaqShown = !isMoreFaqShown; // 상태 변경
            }
        });
    }

    private List<CommunityDTOS.Community_faqDTO> getInitialFaqList() {
        // 초기에 표시할 아이템 리스트 반환 (4개의 아이템만 보여주도록 설정
        List<CommunityDTOS.Community_faqDTO> initialList = new ArrayList<>();
        // 초기에 표시할 아이템을 추가
        for (int i = 0; i < Math.min(4, faqList.size()); i++) {
            initialList.add(faqList.get(i));
            Log.d("TAG", "fff: " + faqList.size());

        }
        return initialList;
    }

    private List<CommunityDTOS.Community_faqDTO> getMoreFaqList() {
        // 더보기를 클릭했을 때 추가로 표시할 아이템 리스트 반환
        List<CommunityDTOS.Community_faqDTO> moreList = new ArrayList<>();

        for (int i = 4; i < faqList.size(); i++) { // 이미 표시된 아이템 이후부터 추가
            moreList.add(faqList.get(i)); // faqList에서 아이템을 가져와서 추가
        }
        return moreList;
    }


    //===========notice======================================================================

//
//    private void initRecyclerViewNotice(LayoutInflater inflater) {
//        noticeAdapter = new Community_NoticeAdapter(LayoutInflater.from(requireContext()), getInitialNoticeList(), requireContext());
//        binding.notice.setAdapter(noticeAdapter);
//        binding.notice.setLayoutManager(new LinearLayoutManager(requireContext()));
//    }
//
//    private void initMoreButtonNotice() {
//        Button moreButton = binding.openNotice;
//        moreButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!isMoreNoticeShown) {
//                    // "더 보기" 버튼이 클릭되었을 때
//                    noticeAdapter.addAll(getMoreNoticeList()); // 추가 아이템 표시
//                    binding.openNotice.setText("접기"); // 버튼 텍스트 변경
//                } else {
//                    // "접기" 버튼이 클릭되었을 때
//
//                    binding.openNotice.setText("더 보기"); // 버튼 텍스트 변경
//                    // 최초 상태로 돌아감
//                    noticeAdapter = new Community_NoticeAdapter(LayoutInflater.from(requireContext()), getInitialNoticeList(), requireContext());
//                    binding.notice.setAdapter(faqAdapter);
//                    binding.notice.setLayoutManager(new LinearLayoutManager(requireContext()));
//
//                    binding.scrollView.smoothScrollTo(0, binding.view3.getTop());
//
//                }
//                isMoreNoticeShown = !isMoreNoticeShown; // 상태 변경
//            }
//        });
//    }
//
//    private List<CommunityDTOS.Community_NoticeDTO> getInitialNoticeList() {
//        // 초기에 표시할 아이템 리스트 반환 (4개의 아이템만 보여주도록 설정
//        List<CommunityDTOS.Community_NoticeDTO> initialList = new ArrayList<>();
//        // 초기에 표시할 아이템을 추가
//        for (int i = 0; i < Math.min(4, notiList.size()); i++) {
//            initialList.add(notiList.get(i));
//            Log.d("TAG", "fff: " + notiList.size());
//
//        }
//        return initialList;
//    }
//
//    private List<CommunityDTOS.Community_NoticeDTO> getMoreNoticeList() {
//        // 더보기를 클릭했을 때 추가로 표시할 아이템 리스트 반환
//        List<CommunityDTOS.Community_NoticeDTO> moreList = new ArrayList<>();
//
//        for (int i = 4; i < notiList.size(); i++) { // 이미 표시된 아이템 이후부터 추가
//            moreList.add(notiList.get(i)); // faqList에서 아이템을 가져와서 추가
//        }
//        return moreList;
//    }
}
