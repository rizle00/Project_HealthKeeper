package com.example.healthkeeper.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthkeeper.App;
import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.common.CommonRepository;
import com.example.healthkeeper.databinding.ItemRecvPushBinding;
import com.example.healthkeeper.databinding.RecvCommunityQuestionBinding;
import com.example.healthkeeper.main.community.CommunityDTOS;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PushAdapter extends RecyclerView.Adapter<PushAdapter.ViewHolder> {

    LayoutInflater inflater;
    Context context;
    public List<AlarmLogVO> mDataset;
   private CommonRepository repository;
   private String id;
    private OnDataChangedListener onDataChangedListener;



    public PushAdapter(LayoutInflater inflater, List<AlarmLogVO> list, Context context, CommonRepository repository, String id, OnDataChangedListener listener) {
        this.inflater = inflater;
        this.context = context;
        this.mDataset = list;
        this.repository = repository;
        this.id = id;
        this.onDataChangedListener = listener;
    }

    public interface OnDataChangedListener {
        void onDataChanged();
    }
    // 데이터 변경 시 콜백 호출
    private void notifyDataChanged() {
        if (onDataChangedListener != null) {
            onDataChangedListener.onDataChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecvPushBinding binding = ItemRecvPushBinding.inflate(inflater, parent, false);


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

    public void removeItem(int position) {
        Log.d("TAG", "removeItem: "+mDataset.get(position).getALARM_ID());
        CommonConn conn = new CommonConn("update/alarm");
        conn.addParamMap("alarm", mDataset.get(position).getALARM_ID());
        conn.addParamMap("id", id );
        repository.insert(conn).thenAccept(result->{

        });
        mDataset.remove(position);
        notifyDataChanged();
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemRecvPushBinding binding;

        public ViewHolder(@NonNull ItemRecvPushBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);

        }

        public void bind(AlarmLogVO item) {

            binding.tvTitle.setText(item.getTypeVO().getTITLE());
            binding.tvTime.setText(item.getTIME());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // 해당 아이템에 대한 작업 수행 (여기서는 다이얼로그 띄우는 코드를 작성)
                showDialog(position);
            }

        }

        private void showDialog(int position) {
            // 다이얼로그를 띄우는 코드 작성
            // 여기서는 AlertDialog를 예시로 사용하겠습니다.
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("알람 정보");
            builder.setMessage("알람 내용: " + mDataset.get(position).getTypeVO().getCONTENT());
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 확인 버튼을 눌렀을 때 수행할 작업 (예: 다이얼로그 닫기)
                    removeItem(position);
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}

