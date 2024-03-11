package com.example.healthkeeper.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthkeeper.R;
import com.example.healthkeeper.databinding.FragmentBoardDetailBinding;

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

        List<CommunityDTOS.Community_BoardDTO> allList = communityDAO.getBoardArrayList();
        Community_boardAdapter allBoardAdapter = new Community_boardAdapter(inflater, (ArrayList<CommunityDTOS.Community_BoardDTO>) communityDAO.getBoardArrayList(), getContext());
        binding.boardDetailList.setAdapter(allBoardAdapter);
        binding.boardDetailList.setLayoutManager(new LinearLayoutManager(getContext()));




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
      return (ArrayList<CommunityDTOS.Community_BoardDTO>) communityDAO.getBoardArrayList();
    }



    private void MainActivity(){
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        getActivity().finish();

    }



}