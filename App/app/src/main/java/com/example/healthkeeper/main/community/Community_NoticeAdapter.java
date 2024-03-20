package com.example.healthkeeper.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.RecvCommunityNoticeBinding;

import java.util.List;

public class Community_NoticeAdapter extends RecyclerView.Adapter<Community_NoticeAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<CommunityDTOS.Community_NoticeDTO> list;
    Context context;

    public Community_NoticeAdapter(LayoutInflater inflater, List<CommunityDTOS.Community_NoticeDTO> list, Context context){
        this.context=context;
        this.list=list;
        this.inflater=inflater;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecvCommunityNoticeBinding binding=RecvCommunityNoticeBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        holder.binding.tvNoticeTitle.setText(list.get(i).getTITLE());
        holder.binding.tvDate.setText(list.get(i).getTIME());
        holder.binding.tvContent.setText(list.get(i).getCONTENT());

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
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecvCommunityNoticeBinding binding;
        public ViewHolder(@NonNull RecvCommunityNoticeBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
