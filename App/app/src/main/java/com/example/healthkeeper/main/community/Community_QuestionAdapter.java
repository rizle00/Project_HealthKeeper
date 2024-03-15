package com.example.healthkeeper.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.databinding.RecvCommunityQuestionBinding;

import java.util.ArrayList;
import java.util.List;

public class Community_QuestionAdapter extends RecyclerView.Adapter<Community_QuestionAdapter.ViewHolder> {

    LayoutInflater inflater;
    Context context;
   List<CommunityDTOS.Community_QuestionDTO> list;


    public Community_QuestionAdapter(LayoutInflater inflater, List<CommunityDTOS.Community_QuestionDTO> list, Context context) {
        this.inflater=inflater;
        this.context=context;
        this.list=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecvCommunityQuestionBinding binding= RecvCommunityQuestionBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.binding.tvAnswer.setText(list.get(i).getAnswer());
        holder.binding.tvQuestion.setText(list.get(i).getQuestion());

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecvCommunityQuestionBinding binding;
        public ViewHolder(@NonNull RecvCommunityQuestionBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
