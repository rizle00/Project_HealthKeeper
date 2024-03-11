package com.example.healthkeeper.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.healthkeeper.databinding.FragmentCommunityBinding;

import java.util.ArrayList;


public class CommunityFragment extends Fragment {
    FragmentCommunityBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // 게시판 어뎁터 초기화
        Community_boardAdapter boardAdapter = new Community_boardAdapter(inflater,getBoardArrayList(), getContext());
        binding.board.setAdapter(boardAdapter);
        binding.board.setLayoutManager(new LinearLayoutManager(getContext()));
           /*(new Community_boardAdapter(inflater,getBoardArrayList(),getContext()))*/

        //자주묻는 질문 어뎁터
        binding.question.setAdapter(new Community_QuestionAdapter(inflater,getQuestionArrayList(),getContext()));
        binding.question.setLayoutManager((new LinearLayoutManager(getContext())));

        //공지사항 어뎁터
        binding.notice.setAdapter(new Community_NoticeAdapter(inflater,getNoticeArrayList(),getContext()));
        binding.notice.setLayoutManager(new LinearLayoutManager(getContext()));



        int threshold1 = 400;// threshold1 :버튼의 선택 상태를 변경하는 데 사용되는 임계값
        int threshold2 = 600;
        int threshold3 = 760;


        binding.scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 스크롤 위치에 따라 버튼 선택 상태 변경
                Log.d("ScrollView", "ScrollY: " + scrollY);

                Log.d("ButtonState", "Button1: " + binding.button1.isSelected() +
                        " Button2: " + binding.button2.isSelected() +
                        " Button3: " + binding.button3.isSelected());

                if (scrollY >= 0 && scrollY < threshold1) {
                    //게시판
                    binding.button1.setSelected(true);
                    binding.button2.setSelected(false);
                    binding.button3.setSelected(false);
                } else if (scrollY >= threshold1 && scrollY < threshold2) {
                    //자주묻는 질문
                    binding.button1.setSelected(false);
                    binding.button2.setSelected(true);
                    binding.button3.setSelected(false);
                } else if (scrollY >= threshold2) {
                    //공지사항
                    binding.button1.setSelected(false);
                    binding.button2.setSelected(false);
                    binding.button3.setSelected(true);
                }

            }
        });

        return view;


    }



    private ArrayList<CommunityDTOS.Community_BoardDTO> getBoardArrayList() {
        ArrayList<CommunityDTOS.Community_BoardDTO> list = new ArrayList<>();
        list.add(new CommunityDTOS().new Community_BoardDTO("오늘은 웨어러블 산날! 기분좋아","황승은","18:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("ㅋㅋ! 기분좋아","이정은","16:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("월요일","송차은","15:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("화요일","황지정","13:29"));
        list.add(new CommunityDTOS().new Community_BoardDTO("수요일","하지매","12:03"));
        list.add(new CommunityDTOS().new Community_BoardDTO("목요일","마시뗴","09:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("금요일","와따시와","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("토요일","하지원","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("일요일","데스네","2020.03.01"));
        list.add(new CommunityDTOS().new Community_BoardDTO("오늘은 웨어러블 산날! 기분좋아","황승은","18:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("ㅋㅋ! 기분좋아","이정은","16:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("월요일","송차은","15:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("화요일","황지정","13:29"));
        list.add(new CommunityDTOS().new Community_BoardDTO("수요일","하지매","12:03"));
        list.add(new CommunityDTOS().new Community_BoardDTO("목요일","마시뗴","09:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("금요일","와따시와","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("토요일","하지원","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("일요일","데스네","2020.03.01"));
        list.add(new CommunityDTOS().new Community_BoardDTO("오늘은 웨어러블 산날! 기분좋아","황승은","18:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("ㅋㅋ! 기분좋아","이정은","16:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("월요일","송차은","15:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("화요일","황지정","13:29"));
        list.add(new CommunityDTOS().new Community_BoardDTO("수요일","하지매","12:03"));
        list.add(new CommunityDTOS().new Community_BoardDTO("목요일","마시뗴","09:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("금요일","와따시와","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("토요일","하지원","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("일요일","데스네","2020.03.01"));
        list.add(new CommunityDTOS().new Community_BoardDTO("오늘은 웨어러블 산날! 기분좋아","황승은","18:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("ㅋㅋ! 기분좋아","이정은","16:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("월요일","송차은","15:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("화요일","황지정","13:29"));
        list.add(new CommunityDTOS().new Community_BoardDTO("수요일","하지매","12:03"));
        list.add(new CommunityDTOS().new Community_BoardDTO("목요일","마시뗴","09:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("금요일","와따시와","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("토요일","하지원","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("일요일","데스네","2020.03.01"));
        list.add(new CommunityDTOS().new Community_BoardDTO("오늘은 웨어러블 산날! 기분좋아","황승은","18:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("ㅋㅋ! 기분좋아","이정은","16:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("월요일","송차은","15:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("화요일","황지정","13:29"));
        list.add(new CommunityDTOS().new Community_BoardDTO("수요일","하지매","12:03"));
        list.add(new CommunityDTOS().new Community_BoardDTO("목요일","마시뗴","09:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("금요일","와따시와","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("토요일","하지원","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("일요일","데스네","2020.03.01"));
        list.add(new CommunityDTOS().new Community_BoardDTO("오늘은 웨어러블 산날! 기분좋아","황승은","18:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("ㅋㅋ! 기분좋아","이정은","16:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("월요일","송차은","15:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("화요일","황지정","13:29"));
        list.add(new CommunityDTOS().new Community_BoardDTO("수요일","하지매","12:03"));
        list.add(new CommunityDTOS().new Community_BoardDTO("목요일","마시뗴","09:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("금요일","와따시와","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("토요일","하지원","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("일요일","데스네","2020.03.01"));
        list.add(new CommunityDTOS().new Community_BoardDTO("오늘은 웨어러블 산날! 기분좋아","황승은","18:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("ㅋㅋ! 기분좋아","이정은","16:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("월요일","송차은","15:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("화요일","황지정","13:29"));
        list.add(new CommunityDTOS().new Community_BoardDTO("수요일","하지매","12:03"));
        list.add(new CommunityDTOS().new Community_BoardDTO("목요일","마시뗴","09:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("금요일","와따시와","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("토요일","하지원","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("일요일","데스네","2020.03.01"));
        list.add(new CommunityDTOS().new Community_BoardDTO("오늘은 웨어러블 산날! 기분좋아","황승은","18:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("ㅋㅋ! 기분좋아","이정은","16:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("월요일","송차은","15:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("화요일","황지정","13:29"));
        list.add(new CommunityDTOS().new Community_BoardDTO("수요일","하지매","12:03"));
        list.add(new CommunityDTOS().new Community_BoardDTO("목요일","마시뗴","09:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("금요일","와따시와","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("토요일","하지원","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("일요일","데스네","2020.03.01"));
        list.add(new CommunityDTOS().new Community_BoardDTO("오늘은 웨어러블 산날! 기분좋아","황승은","18:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("ㅋㅋ! 기분좋아","이정은","16:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("월요일","송차은","15:30"));
        list.add(new CommunityDTOS().new Community_BoardDTO("화요일","황지정","13:29"));
        list.add(new CommunityDTOS().new Community_BoardDTO("수요일","하지매","12:03"));
        list.add(new CommunityDTOS().new Community_BoardDTO("목요일","마시뗴","09:20"));
        list.add(new CommunityDTOS().new Community_BoardDTO("금요일","와따시와","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("토요일","하지원","어제"));
        list.add(new CommunityDTOS().new Community_BoardDTO("일요일","데스네","2020.03.01"));


        return list;
    }

    private ArrayList<CommunityDTOS.Community_QuestionDTO> getQuestionArrayList(){
        ArrayList<CommunityDTOS.Community_QuestionDTO> list= new ArrayList<>();
        list.add(new CommunityDTOS().new Community_QuestionDTO("웨어러블 어떤 기능이있나요?","웨어러블 측정기의 대중화로 최근 신종 코로나바이러스 감염증(코로나19) 여파로 고령자.기저질환자들의 병.의원 방문이 제한되자 환자의 몸에 부착해 일상생활 중에 심장 상태를 모니 터링하는 심전도 기기가 주목받고 있는 것이다. 심전도 검사는 피부에 부착한 전극으로 심장의 전기신호를 측정하기 때문에 병원에서 이뤄지는 짧은 시간 검사로는 부정맥이 잘 발견되지 않는다. 이에 따라 부정맥이 의심될 때는 24시간 심전도를 기록하는 '홀터 검사'가 필요하다."));
        list.add(new CommunityDTOS().new Community_QuestionDTO("나이가 많으신 부모님이 설치 어렵지않을까요??","저의 Smart HealthKeeper 에서는 고령층이나 스마트폰을 이용하시기 힘드신 분들을 위해서 고객을 위한서비스! 찾아가는 서비스!를 시행하고 있습니다. 처음 설치부분에서 1년동안은 저희가 직접 설치나 AS관련사항에대해 직접 방문이 가능합니다."));
        list.add(new CommunityDTOS().new Community_QuestionDTO("위급상황이 생길떈 어떤 조치가 이루어지나요?","착용시 계속적인 측정과 데이터가 저장되고 위험수준의 상황이 생기게되면 위험이라는 알림이 뜨고 119로 바로 전화가 연결되도록 하였습니다. 이러한 위급상황에 병원으로 빠른 이송을 하는것이 저희의 목표입니다. "));

        return list;
    }

    private ArrayList<CommunityDTOS.Community_NoticeDTO> getNoticeArrayList() {
        ArrayList<CommunityDTOS.Community_NoticeDTO> list=new ArrayList<>();
        list.add(new CommunityDTOS().new Community_NoticeDTO("필독","앱관련 공지사항","2020.03.04","17:00"));
        list.add(new CommunityDTOS().new Community_NoticeDTO("정보","웨어러블 뇌졸증미리예방센서 개발!!","2020.03.02","10:00"));
        list.add(new CommunityDTOS().new Community_NoticeDTO("필독","2024.02.23 앱 업데이트","2020.01.31","3:40"));

        return list;
    }


}