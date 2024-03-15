package com.example.healthkeeper.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.databinding.RecvCommunityBoardBinding;

import java.util.ArrayList;
import java.util.List;

public class Community_boardAdapter extends RecyclerView.Adapter<Community_boardAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<CommunityDTOS.Community_BoardDTO> List;
    Context context;

    public Community_boardAdapter (LayoutInflater inflater, List<CommunityDTOS.Community_BoardDTO>  boardList, Context context){
        this.context=context;
        this. List= boardList;
        this.inflater=inflater;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @NonNull RecvCommunityBoardBinding binding=RecvCommunityBoardBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        CommunityDTOS.Community_BoardDTO item=List.get(i);
        holder.bind(item);


    }

    @Override
    public int getItemCount() {

        return List.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RecvCommunityBoardBinding binding;
        public ViewHolder(@NonNull RecvCommunityBoardBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void bind(CommunityDTOS.Community_BoardDTO item) {
            // 데이터 바인딩
            binding.tvTitle.setText(item.getTitle());//게시글 제목
            binding.tvWriter.setText(item.getWriter());//게시글 작성자
            binding.tvWriteTime.setText(item.getTime());//게시글 작성 시간
            binding.tvContent.setText(item.getContent());//게시글내용
            binding.tvComments.setText(item.getComments());//댓글수 표현

            binding.clickContentOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.tvContent.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
