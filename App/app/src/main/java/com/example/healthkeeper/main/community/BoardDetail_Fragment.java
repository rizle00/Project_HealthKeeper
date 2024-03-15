package com.example.healthkeeper.main.community;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.databinding.FragmentBoardDetailBinding;
import com.example.healthkeeper.main.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class BoardDetail_Fragment extends Fragment {
    private static final String TAG = BoardDetail_Fragment.class.getSimpleName();
    FragmentBoardDetailBinding binding;
    CommunityDAO communityDAO;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
               binding=FragmentBoardDetailBinding.inflate(inflater, container, false);
                View view=binding.getRoot();


        CommunityDAO communityDAO = new CommunityDAO();

        List<CommunityDTOS.Community_BoardDTO> allList = communityDAO.getBoardList();
        Community_boardAdapter allBoardAdapter = new Community_boardAdapter(inflater, allList, getContext());
        binding.boardDetailList.setAdapter(allBoardAdapter);
        binding.boardDetailList.setLayoutManager(new LinearLayoutManager(getContext()));

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






    private ArrayList<CommunityDTOS.Community_BoardDTO> getBoardArrayList() {
      return (ArrayList<CommunityDTOS.Community_BoardDTO>) communityDAO.getBoardList();
    }



    private void MainActivity(){
        Intent intent=new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();

    }



}