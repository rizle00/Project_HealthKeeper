package com.example.healthkeeper.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthkeeper.databinding.ItemRecvPushBinding;
import com.example.healthkeeper.databinding.RecvCommunityQuestionBinding;
import com.example.healthkeeper.main.community.CommunityDTOS;

import java.util.List;

public class PushAdapter extends RecyclerView.Adapter<PushAdapter.ViewHolder> {

    LayoutInflater inflater;
    Context context;
    public List<AlarmLogVO> mDataset;


    public PushAdapter(LayoutInflater inflater, List<AlarmLogVO> list, Context context) {
        this.inflater=inflater;
        this.context=context;
        this.mDataset=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecvPushBinding binding= ItemRecvPushBinding.inflate(inflater,parent,false);


        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        if (mDataset == null) return; // List가 null이면 종료
        AlarmLogVO item = mDataset.get(i);
        // 질문 정보를 ViewHolder에 바인딩
        holder.bind(item);


    }



    @Override
    public int getItemCount() {

        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemRecvPushBinding binding;
        public ViewHolder(@NonNull ItemRecvPushBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
        public void bind(AlarmLogVO item) {

            binding.tvTitle.setText(item.getTypeVO().getTITLE());
            binding.tvTime.setText(item.getTIME());
        }
    }
}
