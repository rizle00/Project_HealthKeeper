package com.example.healthkeeper.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.databinding.RecvCommunityBoardBinding;
import com.example.healthkeeper.main.CommunityDTOS.Community_BoardDTO;

import java.util.ArrayList;

public class Community_BoardAdapter extends RecyclerView.Adapter<Community_BoardAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    ArrayList<CommunityDTOS.Community_BoardDTO> list;


    public Community_BoardAdapter(LayoutInflater inflater, ArrayList<CommunityDTOS.Community_BoardDTO> list, Context context) {
        this.inflater = inflater;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecvCommunityBoardBinding binding = RecvCommunityBoardBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.binding.tvWriter.setText(list.get(i).getWriter());
        holder.binding.tvTitle.setText(list.get(i).getTitle());
        holder.binding.tvWriteTime.setText(list.get(i).getTime());
        holder.binding.tvComments.setText(list.get(i).getComments());

       /* holder.binding.tvTitle.setOnClickListener(v->{           //,클릭해서 자유게시판만 디테일하게 보게할지
                                                                        아니면 지금화면에 나타낼지 고려중...
            Intent intent=new Intent(context,BoardDetailActivity.class);
            context.startActivity(intent);
        });*/


    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecvCommunityBoardBinding binding;

        public ViewHolder(RecvCommunityBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }
}