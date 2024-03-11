package com.example.healthkeeper.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.databinding.RecvCommunityBoardBinding;

import java.util.ArrayList;

public class Community_boardAdapter extends RecyclerView.Adapter<Community_boardAdapter.ViewHolder> {
    LayoutInflater inflater;
    ArrayList<CommunityDTOS.Community_BoardDTO> list;
    Context context;

    public Community_boardAdapter (LayoutInflater inflater, ArrayList<CommunityDTOS.Community_BoardDTO> list, Context context){
        this.context=context;
        this.list=list;
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
        CommunityDTOS.Community_BoardDTO item=list.get(i);
        holder.bind(item);


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecvCommunityBoardBinding binding;
        public ViewHolder(@NonNull RecvCommunityBoardBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void bind(CommunityDTOS.Community_BoardDTO item) {
            // 데이터 바인딩
            binding.tvTitle.setText(item.getTitle());
            binding.tvWriter.setText(item.getWriter());
            binding.tvWriteTime.setText(item.getTime());
        }
    }
}