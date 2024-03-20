package com.example.healthkeeper.main.community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthkeeper.App;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.RecvCommunityQuestionBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Community_QuestionAdapter extends RecyclerView.Adapter<Community_QuestionAdapter.ViewHolder> {

    LayoutInflater inflater;
    Context context;
   List<CommunityDTOS.Community_QuestionDTO> List;
    private List<CommunityDTOS.AnswerDTO> answerList;
    CommonRepository repository;


    public Community_QuestionAdapter(LayoutInflater inflater,CommonRepository repository, List<CommunityDTOS.Community_QuestionDTO> list, Context context) {
        this.inflater=inflater;
        this.context=context;
        this.List=list;
//        this.answerList=answerList;
        this.repository = repository;

        for (CommunityDTOS.Community_QuestionDTO question : List) {
            answerList(question.getQUE_ID());
        }


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
        // 해당 질문에 대한 답변을 가져오기
//        answerList(item.getQUE_ID(), holder);

        // 질문 정보를 ViewHolder에 바인딩
        holder.bind(item);
        holder.loadAnswer(item.getQUE_ID());


//        answerList = new ArrayList<>();

//        item.getQUE_ID().equals()
//        if (answerList == null || i >= answerList.size()) {
//            holder.bind(item, null); // answerList가 null이거나 해당 인덱스의 답변이 없는 경우
//        } else {
//            CommunityDTOS.AnswerDTO item_answer = answerList.get(i);
//            holder.bind(item, item_answer);
//        }
    }
    private void answerList(String questionId, ViewHolder holder){
        CommonConn conn4 = new CommonConn("question/answer");
        conn4.addParamMap("params", questionId);
        repository.select(conn4).thenAccept(result -> {
            // 서버에서 답변을 가져온 후에 실행될 작업들을 여기에 작성
            CommunityDTOS.AnswerDTO answer = new Gson().fromJson(result, CommunityDTOS.AnswerDTO.class);

            // 가져온 답변을 ViewHolder에 전달하여 UI 업데이트
            holder.updateAnswer(answer);
        });
    }
    private void answerList(String params){
        CommonConn conn4 = new CommonConn("question/answer");
        conn4.addParamMap("params", params);
        repository.select(conn4).thenAccept(result ->{
            CommunityDTOS.AnswerDTO answer = new Gson().fromJson(result, CommunityDTOS.AnswerDTO.class);
            Log.d("TAG", "bbb: "+params);
            Log.d("TAG", "bbb: "+answer.getQUE_ID());
            answerList.add(answer);

        });
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
        public void loadAnswer(String questionId) {
            CommonConn conn4 = new CommonConn("question/answer");
            conn4.addParamMap("params", questionId);
            repository.select(conn4).thenAccept(result -> {

                CommunityDTOS.AnswerDTO answer = new Gson().fromJson(result, CommunityDTOS.AnswerDTO.class);
                updateAnswer(answer);
                Log.d("TAG1", "answerList: "+questionId);
                Log.d("TAG1", "answerList: "+answer.getQUE_ID());
            });
        }
        public void updateAnswer(CommunityDTOS.AnswerDTO answer) {
            // 여기에 가져온 답변을 이용하여 UI를 업데이트하는 작업을 수행
            if (answer != null) {
                binding.tvReadCnt.setVisibility(View.VISIBLE);
                binding.tvAnswerContent.setText(answer.getANSWER_CONTENT());
                binding.tvAnswerTime.setText(answer.getTIME());
            } else {
                binding.tvReadCnt.setVisibility(View.GONE);
            }
        }
        public void bind(CommunityDTOS.Community_QuestionDTO item) {
            Log.d("TAG", "bind: "+item.getTITLE());
            Log.d("TAG", "bind: "+item.getMEMBER_ID());
            binding.tvTile.setText(item.getTITLE());
            binding.tvWriter.setText(item.getMEMBER_ID());
            binding.tvWriteTime.setText(item.getTIME());
            binding.tvContent.setText(item.getCONTENT());
//            binding.tvAnswerContent.setText(item_answer.getANSWER_CONTENT());
//            binding.tvAnswerTime.setText(item_answer.getTIME());





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
