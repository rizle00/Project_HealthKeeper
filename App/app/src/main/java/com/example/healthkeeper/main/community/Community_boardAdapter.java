package com.example.healthkeeper.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.databinding.RecvCommunityBoardBinding;

import java.util.ArrayList;

public class Community_boardAdapter extends RecyclerView.Adapter<Community_boardAdapter.ViewHolder> {
    LayoutInflater inflater;
    ArrayList<CommunityDTOS.Community_BoardDTO> List;
    Context context;

    public Community_boardAdapter (LayoutInflater inflater, ArrayList<CommunityDTOS.Community_BoardDTO>  boardList, Context context){
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
