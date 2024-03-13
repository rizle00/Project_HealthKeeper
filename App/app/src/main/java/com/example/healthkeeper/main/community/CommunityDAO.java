package com.example.healthkeeper.main.community;

import java.util.ArrayList;
import java.util.List;

public class CommunityDAO {
    List<CommunityDTOS.Community_BoardDTO> getRecentBoardList() {
        List<CommunityDTOS.Community_BoardDTO> list =getBoardArrayList();
        List<CommunityDTOS.Community_BoardDTO> recentList = new ArrayList<>();
        for (int i = 0; i < Math.min(list.size(), 4); i++) {
            //List의 크기가 4보다 작으면 boardList의 크기만큼만 반복하고, 4 이상이면 4번만 반복하도록!
            recentList.add(list.get(i));
        }
        return recentList;
    }

    public List<CommunityDTOS.Community_BoardDTO> getBoardArrayList() {
        // 전체 게시글 데이터
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



        return list;

    }

    public List<CommunityDTOS.Community_QuestionDTO> getQuestionArrayList() {
        //자주 묻는 질문 데이터
        ArrayList<CommunityDTOS.Community_QuestionDTO> list= new ArrayList<>();
        list.add(new CommunityDTOS().new Community_QuestionDTO("웨어러블 어떤 기능이있나요?","웨어러블 측정기의 대중화로 최근 신종 코로나바이러스 감염증(코로나19) 여파로 고령자.기저질환자들의 병.의원 방문이 제한되자 환자의 몸에 부착해 일상생활 중에 심장 상태를 모니 터링하는 심전도 기기가 주목받고 있는 것이다. 심전도 검사는 피부에 부착한 전극으로 심장의 전기신호를 측정하기 때문에 병원에서 이뤄지는 짧은 시간 검사로는 부정맥이 잘 발견되지 않는다. 이에 따라 부정맥이 의심될 때는 24시간 심전도를 기록하는 '홀터 검사'가 필요하다."));
        list.add(new CommunityDTOS().new Community_QuestionDTO("나이가 많으신 부모님이 설치 어렵지않을까요??","저의 Smart HealthKeeper 에서는 고령층이나 스마트폰을 이용하시기 힘드신 분들을 위해서 고객을 위한서비스! 찾아가는 서비스!를 시행하고 있습니다. 처음 설치부분에서 1년동안은 저희가 직접 설치나 AS관련사항에대해 직접 방문이 가능합니다."));
        list.add(new CommunityDTOS().new Community_QuestionDTO("위급상황이 생길떈 어떤 조치가 이루어지나요?","착용시 계속적인 측정과 데이터가 저장되고 위험수준의 상황이 생기게되면 위험이라는 알림이 뜨고 119로 바로 전화가 연결되도록 하였습니다. 이러한 위급상황에 병원으로 빠른 이송을 하는것이 저희의 목표입니다. "));

        return list;
    }

    public List<CommunityDTOS.Community_NoticeDTO> getNoticeArrayList() {
        //  공지사항 데이터

        ArrayList<CommunityDTOS.Community_NoticeDTO> list=new ArrayList<>();
        list.add(new CommunityDTOS().new Community_NoticeDTO("필독","앱관련 공지사항","2020.03.04","17:00","서비스 점검으로 인하여 2024년03월28일 02:00 ~ 2024년03월 28일 04:00까지 서비스가 일부 작동이 원황하지 않을 수 있습니다. \n 불편을 드려 매우 죄송합니다\n 빠르게 서비스가 원활할게 돌아가도록 하겠습니다."));
        list.add(new CommunityDTOS().new Community_NoticeDTO("정보","웨어러블 뇌졸증 미리 예방센서 개발!!","2020.03.02","10:00","ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇddddddd"));
        list.add(new CommunityDTOS().new Community_NoticeDTO("필독","2024.02.23 앱 업데이트","2020.01.31","3:40","odeielsjfjsdjllasdflasdfasdfasdfatgtadfhkkkkkkkkkkihhhhhhhhhhhhhhhhhiofghhhhhhhhhhhhh"));

        return list;
    }

}
