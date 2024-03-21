package com.example.healthkeeper.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.RecvCommunityFaqBinding;

import java.util.Arrays;
import java.util.List;

public class Community_FaqAdapter extends RecyclerView.Adapter<Community_FaqAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<CommunityDTOS.Community_faqDTO> List;
    Context context;
    private boolean[] isVisibleArray;// 각 아이템의 가시성을 저장하는 배열

    public Community_FaqAdapter(LayoutInflater inflater, List<CommunityDTOS.Community_faqDTO>  boardList, Context context){
        this.context=context;
        this. List= boardList;
        this.inflater=inflater;
        isVisibleArray = new boolean[List.size()]; // 각 아이템의 가시성을 저장하는 배열 초기화

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @NonNull RecvCommunityFaqBinding binding=RecvCommunityFaqBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        CommunityDTOS.Community_faqDTO item=List.get(i);
        holder.bind(item, isVisibleArray[i]);


    }

    @Override
    public int getItemCount() {

        return List.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RecvCommunityFaqBinding binding;
        public ViewHolder(@NonNull RecvCommunityFaqBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void bind(CommunityDTOS.Community_faqDTO item, boolean isVisible) {
            // 데이터 바인딩

            binding.tvTitle.setText(item.getTITLE());//게시글 제목
            binding.tvContent.setText(item.getCONTENT());//게시글내용
            //binding.tvContent.setVisibility(View.VISIBLE);



        }
    }

    // 추가된 모든 아이템을 리스트에 추가하는 메서드
    public void addAll(List<CommunityDTOS.Community_faqDTO> moreList) {
        int startPosition = List.size();
        List.addAll(moreList);
        isVisibleArray = Arrays.copyOf(isVisibleArray, List.size()); // isVisibleArray 배열 크기 조정
        notifyItemRangeInserted(startPosition, moreList.size());
    }

    // 모든 아이템을 숨기는 메서드
    public void hideAll() {
        Arrays.fill(isVisibleArray, false); // isVisibleArray 배열을 모두 false로 설정
        notifyDataSetChanged();
    }
}
