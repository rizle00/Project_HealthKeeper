package com.example.healthkeeper.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.databinding.RecvCommunityQuestionBinding;

import java.util.List;

public class Community_QuestionAdapter extends RecyclerView.Adapter<Community_QuestionAdapter.ViewHolder> {

    LayoutInflater inflater;
    Context context;
   List<CommunityDTOS.Community_QuestionDTO> List;


    public Community_QuestionAdapter(LayoutInflater inflater, List<CommunityDTOS.Community_QuestionDTO> list, Context context) {
        this.inflater=inflater;
        this.context=context;
        this.List=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecvCommunityQuestionBinding binding= RecvCommunityQuestionBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        CommunityDTOS.Community_QuestionDTO item= List.get(i);



    }



    @Override
    public int getItemCount() {
        return List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecvCommunityQuestionBinding binding;
        public ViewHolder(@NonNull RecvCommunityQuestionBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
        public void bind(CommunityDTOS.Community_QuestionDTO item) {
            binding.tvTile.setText(item.getTITLE());
            binding.tvWriter.setText(item.getMEMBER_ID());
            binding.tvWriteTime.setText(item.getTIME());
            binding.tvContent.setText(item.getCONTENT());
            binding.tvReadCnt.setText(item.getREAD_CNT());
            binding.tvAnswer.setText(item.getTIME());


            binding.clickContentOpen.setOnClickListener(new View.OnClickListener() {//게시글 내용보기
                @Override
                public void onClick(View view) {
                    binding.imgMore.setVisibility(View.GONE);
                    binding.tvClickAnswer.setVisibility(View.VISIBLE);
                    binding.imgReadCnt.setVisibility(View.GONE);

                }
            });

            binding.imgLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.imgLess.setVisibility(View.GONE);
                    binding.imgMore.setVisibility(View.VISIBLE);
                    binding.clickContentOpen.setVisibility(View.GONE);

                    /*새댓글  생기면 알려주고 한번  binding.clickContentOpen가 터치되면 Gone처리해야함!! */
                }
            });

        }
    }
}
