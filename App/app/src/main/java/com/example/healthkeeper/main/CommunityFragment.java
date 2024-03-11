package com.example.healthkeeper.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentCommunityBinding;


import com.example.healthkeeper.databinding.FragmentCommunityBinding;

import java.util.ArrayList;
import java.util.List;


public class CommunityFragment extends Fragment {
    FragmentCommunityBinding binding;
    CommunityDAO communityDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        CommunityDAO communityDAO = new CommunityDAO();

                // 게시판 어뎁터
        List<CommunityDTOS.Community_BoardDTO> recentList = communityDAO.getRecentBoardList();
        Community_boardAdapter recentBoardAdapter = new Community_boardAdapter(inflater, (ArrayList<CommunityDTOS.Community_BoardDTO>) communityDAO.getRecentBoardList(), getContext());
        binding.board.setAdapter(recentBoardAdapter);
        binding.board.setLayoutManager(new LinearLayoutManager(getContext()));

        // 자주묻는 질문 어뎁터
        List<CommunityDTOS.Community_QuestionDTO> questionList = communityDAO.getQuestionArrayList();
        binding.question.setAdapter(new Community_QuestionAdapter(inflater, (ArrayList<CommunityDTOS.Community_QuestionDTO>) communityDAO.getQuestionArrayList(),getContext()));
        binding.question.setLayoutManager((new LinearLayoutManager(getContext())));

        // 공지사항 어뎁터
        List<CommunityDTOS.Community_NoticeDTO> noticeList = communityDAO.getNoticeArrayList();
        binding.notice.setAdapter(new Community_NoticeAdapter(inflater, (ArrayList<CommunityDTOS.Community_NoticeDTO>) communityDAO.getNoticeArrayList(),getContext()));
        binding.notice.setLayoutManager(new LinearLayoutManager(getContext()));






        int threshold1 = 400;// threshold1 :버튼의 선택 상태를 변경하는 데 사용되는 임계값
        int threshold2 = 600;
        int threshold3 = 760;

        binding.clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               BoardDetail_Fragment();
            }


          private void BoardDetail_Fragment(){
                BoardDetail_Fragment boardDetailFragment=new BoardDetail_Fragment();
                FragmentTransaction transaction =getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container,boardDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();

          }
        });


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





    private List<CommunityDTOS.Community_BoardDTO> getRecentBoardList() {
        return communityDAO.getRecentBoardList();
    }

    private List<CommunityDTOS.Community_QuestionDTO> getQuestionArrayList() {
        return communityDAO.getQuestionArrayList();
    }

    private List<CommunityDTOS.Community_NoticeDTO> getNoticeArrayList() {
        return communityDAO.getNoticeArrayList();
    }


}