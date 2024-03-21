package com.example.healthkeeper.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.RecvCommunityNoticeBinding;

import java.util.Arrays;
import java.util.List;

public class Community_NoticeAdapter extends RecyclerView.Adapter<Community_NoticeAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<CommunityDTOS.Community_NoticeDTO> List;
    Context context;
    private boolean[] isVisibleArray;// 각 아이템의 가시성을 저장하는 배열

    public Community_NoticeAdapter(LayoutInflater inflater, List<CommunityDTOS.Community_NoticeDTO> list, Context context){
        this.context=context;
        this.List=list;
        this.inflater=inflater;
        isVisibleArray = new boolean[List.size()]; // 각 아이템의 가시성을 저장하는 배열 초기화
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecvCommunityNoticeBinding binding=RecvCommunityNoticeBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        holder.binding.tvNoticeTitle.setText(List.get(i).getTITLE());
        holder.binding.tvDate.setText(List.get(i).getTIME());
        holder.binding.tvContent.setText(List.get(i).getCONTENT());

        holder.binding.tvNoticeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.tvContent.setVisibility(View.VISIBLE);
                holder.binding.imgLess.setVisibility(View.VISIBLE);
                holder.binding.imgMore.setVisibility(View.GONE);
            }
        });
        holder.binding.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.tvContent.setVisibility(View.VISIBLE);
                holder.binding.imgLess.setVisibility(View.VISIBLE);
                holder.binding.imgMore.setVisibility(View.GONE);
            }
        });
        holder.binding.imgLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.tvContent.setVisibility(View.GONE);
                holder.binding.imgLess.setVisibility(View.GONE);
                holder.binding.imgMore.setVisibility(View.VISIBLE);

            }
        });


    }



    @Override
    public int getItemCount() {
        return List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecvCommunityNoticeBinding binding;
        public ViewHolder(@NonNull RecvCommunityNoticeBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    // 추가된 모든 아이템을 리스트에 추가하는 메서드
//    public void addAll(List<CommunityDTOS.Community_NoticeDTO> moreList) {
//        int startPosition = List.size();
//        List.addAll(moreList);
//        isVisibleArray = Arrays.copyOf(isVisibleArray, List.size()); // isVisibleArray 배열 크기 조정
//        notifyItemRangeInserted(startPosition, moreList.size());
//    }
}
