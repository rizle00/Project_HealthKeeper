package com.example.healthkeeper.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.databinding.ItemRecvNoticeBinding;
import com.example.healthkeeper.member.GuardianMemberVO;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.Viewholder>{
    LayoutInflater inflater;
    ArrayList<String> list;


    public NoticeAdapter(LayoutInflater inflater, List<GuardianMemberVO> list) {
        this.inflater = inflater;
        //this.list = list;   !!!!!!!!!!!!!
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecvNoticeBinding binding = ItemRecvNoticeBinding.inflate(inflater,parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder h, int i) {
        h.binding.tvTitle.setText("");
        h.binding.tvPreview.setText("");
        h.binding.tvWriteTime.setText("");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ItemRecvNoticeBinding binding;

        public Viewholder(@NonNull ItemRecvNoticeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
