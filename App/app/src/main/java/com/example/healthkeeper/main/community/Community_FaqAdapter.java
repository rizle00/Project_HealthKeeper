package com.example.healthkeeper.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.healthkeeper.databinding.RecvCommunityFaqBinding;

import java.util.List;

public class Community_FaqAdapter extends RecyclerView.Adapter<Community_FaqAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<CommunityDTOS.Community_faqDTO> List;
    Context context;

    public Community_FaqAdapter(LayoutInflater inflater, List<CommunityDTOS.Community_faqDTO>  boardList, Context context){
        this.context=context;
        this. List= boardList;
        this.inflater=inflater;
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
        holder.bind(item);


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
        public void bind(CommunityDTOS.Community_faqDTO item) {
            // 데이터 바인딩

            binding.tvTitle.setText(item.getTITLE());//게시글 제목
            binding.tvContent.setText(item.getCONTENT());//게시글내용

        }
    }
}
