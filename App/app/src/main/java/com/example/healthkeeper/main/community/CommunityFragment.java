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
import com.example.healthkeeper.bluetooth.ConditionVO;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.FragmentCommunityBinding;
import com.example.healthkeeper.setting.NoticeAdapter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.http.HEAD;


public class CommunityFragment extends Fragment {
    FragmentCommunityBinding binding;
    private boolean isMoreFaqShown = false;//더 보기 버튼 클릭 상태를 나타내는 변수
    private boolean isMoreNoticeShown = false;//더 보기 버튼 클릭 상태를 나타내는 변수

    private List<CommunityDTOS.Community_QuestionDTO> queList;
    private List<CommunityDTOS.Community_NoticeDTO> notiList;
    private List<CommunityDTOS.Community_faqDTO> faqList;
    private Community_FaqAdapter faqAdapter;
    private Community_NoticeAdapter noticeAdapter;
    private Spinner spinner;
    CommonRepository repository;//스프링과 연결...
    private CommunityDTOS.Community_QuestionDTO vo;
   private final SharedPreferences pref = requireActivity().getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        spinner = binding.spinnerCategory;
        repository = new CommonRepository(((App) requireActivity().getApplication()).executorService);
        createQuestion(inflater);


        createFaq(inflater);


        createNotice(inflater);

//        testInsert();


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
                final int[] select = {0};
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
                    categoryNames.add("카테고리");

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
                            select[0] = i;
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


                            //회원id 불러오기, 메인화면에 있는 정보 가져오게해야함.
                            String id = pref.getString("user_id", "익명");//?????????????
                            // 서버로 전송할 데이터 맵 구성


                            vo.setTITLE(title);
                            vo.setCONTENT(content);
                            vo.setMEMBER_ID(id);
                            vo.setCATEGORY_ID(String.valueOf(select[0] - 1));


                            // 서버에 데이터 전송
                            CommonConn write = new CommonConn("question/newWrite");
                            write.addParamMap("params", new Gson().toJson(vo)); // 맵을 JSON 문자열로 변환하여 전송
                            repository.select(write).thenAccept(result -> {

                                if (result != null) {
                                    // 성공적으로 데이터가 전송되었을 때의 처리
                                    Toast.makeText(getContext(), "글이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    createQuestion(inflater);

                                    // 입력 폼 숨기기
                                    binding.tvNewWritingShow.setVisibility(View.GONE);
                                } else {
                                    // 전송 실패 시의 처리
                                    Toast.makeText(getContext(), "글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });

                binding.cancleNewWrite.setOnClickListener(v -> {
                    binding.tvNewWritingShow.setVisibility(View.GONE);
                });
            }
        });


//====================================================================================================
        initButton();

        return view;
    }

    private void createNotice(LayoutInflater inflater) {
        CommonConn conn3 = new CommonConn("notice/list");
        repository.select(conn3).thenAccept(result -> {
            notiList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_NoticeDTO>>() {
            }.getType());

            initRecyclerViewNotice(inflater);
            getInitialNoticeList();
            initMoreButtonNotice(inflater);
        });
    }

    private void createFaq(LayoutInflater inflater) {
        CommonConn conn2 = new CommonConn("faq/list");
        repository.select(conn2).thenAccept(result -> {
            faqList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_faqDTO>>() {
            }.getType());

            initRecyclerViewFaq(inflater);
            getInitialFaqList();
            initMoreButtonFaq(inflater);
        });
    }

    private void testInsert() {
        CommonConn test = new CommonConn("insertCondition");
        ConditionVO vo2 = new ConditionVO();
        vo2.setCONDITION_ID("7");
        vo2.setCONDITION_PULSE("123");
        vo2.setCONDITION_TEMPERATURE("36");
        vo2.setMEMBER_ID("2");

        vo2.setCONDITION_ACCIDENT("N");

        test.addParamMap("params", new Gson().toJson(vo2));
        repository.insert(test).thenAccept(result -> {
            Log.d("TAG", "test: " + result);
        });
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
                vo.setSECRET("y");
            }
        });

        // 공개글 선택 시
        binding.radioButtonPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vo.setSECRET("n");
            }
        });

    }

    private void createQuestion(LayoutInflater inflater) {
        Log.d("TAG", "aaaa:질문 ");
        CommonConn conn = new CommonConn("question/list");
        conn.addParamMap("params", 5);
        repository.select(conn).thenAccept(result -> {

            queList = new Gson().fromJson(result, new TypeToken<List<CommunityDTOS.Community_QuestionDTO>>() {
            }.getType());
            Log.d("TAG", "aaaa: " + queList.size());
            Log.d("TAG", "aaaa: " + queList.get(0).getDto().getANSWER_CONTENT());

            // RecyclerView 어댑터 설정을 여기서 수행
            Community_QuestionAdapter questionAdapter = new Community_QuestionAdapter(inflater, queList, getContext(), pref.getString("user_id","") );

            binding.question.setAdapter(questionAdapter);
            binding.question.setLayoutManager((new LinearLayoutManager(getContext())));

            // queList에서 각 Community_QuestionDTO 객체의 id 값을 추출하여 리스트에 추가


//            createque4(result, inflater);//질문게시판
        });
    }



    //==============Faq====================================================================

    private void initRecyclerViewFaq(LayoutInflater inflater) {
        faqAdapter = new Community_FaqAdapter(inflater, getInitialFaqList(), getContext());
        binding.faq.setAdapter(faqAdapter);
        binding.faq.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initMoreButtonFaq(LayoutInflater inflater) {
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
                    faqAdapter = new Community_FaqAdapter(inflater, getInitialFaqList(), getContext());
                    binding.faq.setAdapter(faqAdapter);
                    binding.faq.setLayoutManager(new LinearLayoutManager(getContext()));

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


    private void initRecyclerViewNotice(LayoutInflater inflater) {
        noticeAdapter = new Community_NoticeAdapter(inflater,getInitialNoticeList() ,getContext());
        binding.notice.setAdapter(noticeAdapter);
        binding.notice.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initMoreButtonNotice(LayoutInflater inflater) {
        Button moreButton = binding.openNotice;
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isMoreNoticeShown) {
                    // "더 보기" 버튼이 클릭되었을 때
                    noticeAdapter.addAll(getMoreNoticeList()); // 추가 아이템 표시
                    binding.openNotice.setText("접기"); // 버튼 텍스트 변경
                } else {
                    // "접기" 버튼이 클릭되었을 때

                    binding.openNotice.setText("더 보기"); // 버튼 텍스트 변경
                    // 최초 상태로 돌아감
                    noticeAdapter = new Community_NoticeAdapter(inflater, getInitialNoticeList(), requireContext());
                    binding.notice.setAdapter(faqAdapter);
                    binding.notice.setLayoutManager(new LinearLayoutManager(requireContext()));

                    binding.scrollView.smoothScrollTo(0, binding.view3.getTop());

                }
                isMoreNoticeShown = !isMoreNoticeShown; // 상태 변경
            }
        });
    }

    private List<CommunityDTOS.Community_NoticeDTO> getInitialNoticeList() {
        // 초기에 표시할 아이템 리스트 반환 (4개의 아이템만 보여주도록 설정
        List<CommunityDTOS.Community_NoticeDTO> initialList = new ArrayList<>();
        // 초기에 표시할 아이템을 추가
        for (int i = 0; i < Math.min(4, notiList.size()); i++) {
            initialList.add(notiList.get(i));
            Log.d("TAG", "fff: " + notiList.size());

        }
        return initialList;
    }

    private List<CommunityDTOS.Community_NoticeDTO> getMoreNoticeList() {
        // 더보기를 클릭했을 때 추가로 표시할 아이템 리스트 반환
        List<CommunityDTOS.Community_NoticeDTO> moreList = new ArrayList<>();

        for (int i = 4; i < notiList.size(); i++) { // 이미 표시된 아이템 이후부터 추가
            moreList.add(notiList.get(i)); // faqList에서 아이템을 가져와서 추가
        }
        return moreList;
    }
}





