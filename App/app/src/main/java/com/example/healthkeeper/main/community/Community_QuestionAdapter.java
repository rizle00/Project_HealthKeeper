package com.example.healthkeeper.main.community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.RecvCommunityQuestionBinding;
import com.google.gson.Gson;

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
        if (List == null) return; // List가 null이면 종료
        CommunityDTOS.Community_QuestionDTO item = List.get(i);
        Log.d("TAG", "aaaa: "+ List.size());

        // 질문 정보를 ViewHolder에 바인딩
        holder.bind(item);


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
            binding.tvAnswerContent.setText(item.getDto().getANSWER_CONTENT());
            binding.tvAnswerTime.setText(item.getDto().getTIME());
            Log.d("TAG", "qqqqqqq: "+item.getDto().getANSWER_CONTENT());
// tvReadCnt 클릭 이벤트 처리
            binding.tvReadCnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // tvReadCnt를 클릭했을 때 tvReadCnt가 gone 상태로 변경
                    binding.tvReadCnt.setVisibility(View.GONE);
                    binding.imgMore.setVisibility(View.GONE);
                    binding.tvReadContent.setVisibility(View.VISIBLE);
                    binding.imgLess.setVisibility(View.VISIBLE);
                    binding.imgAnswer.setVisibility(View.VISIBLE);
                    binding.tvAnswerTitle.setVisibility(View.VISIBLE);
                    binding.tvAnswerTitle.setVisibility(View.VISIBLE);
                    binding.tvAnswerContent.setVisibility(View.VISIBLE);
                }
            });

                // tvAnswerContent가 새롭게 만들어질 때 tvReadCnt를 visible 상태로 변경
            if (binding.tvAnswerContent.getVisibility() == View.VISIBLE) {
                // tvAnswerContent가 이미 있는 경우
                // tvReadCnt를 visible 상태로 변경
                binding.tvReadCnt.setVisibility(View.VISIBLE);
            } else {
                // tvAnswerContent가 새롭게 만들어진 경우
                // tvReadCnt를 gone 상태로 유지
                binding.tvReadCnt.setVisibility(View.GONE);
            }








            binding.tvTile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.tvReadCnt.setVisibility(View.GONE);
                    binding.imgMore.setVisibility(View.GONE);
                    binding.tvReadContent.setVisibility(View.VISIBLE);
                    binding.imgLess.setVisibility(View.VISIBLE);
                    binding.imgAnswer.setVisibility(View.VISIBLE);
                    binding.tvAnswerTitle.setVisibility(View.VISIBLE);
                    binding.tvAnswerTitle.setVisibility(View.VISIBLE);
                    binding.tvAnswerContent.setVisibility(View.VISIBLE);
                }
            });
            binding.imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.imgMore.setVisibility(View.GONE);
                    binding.tvReadContent.setVisibility(View.VISIBLE);
                    binding.tvReadCnt.setVisibility(View.GONE);
                    binding.imgLess.setVisibility(View.VISIBLE);
                    binding.imgAnswer.setVisibility(View.VISIBLE);
                    binding.tvAnswerTitle.setVisibility(View.VISIBLE);
                    binding.tvAnswerTitle.setVisibility(View.VISIBLE);
                    binding.tvAnswerContent.setVisibility(View.VISIBLE);

                }
            });



            binding.imgLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.imgLess.setVisibility(View.GONE);
                    binding.imgMore.setVisibility(View.VISIBLE);
                    binding.tvReadContent.setVisibility(View.GONE);

                    /*새댓글  생기면 알려주고 한번  binding.clickContentOpen가 터치되면 Gone처리해야함!! */
                }
            });

        }
    }
}
