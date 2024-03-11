package com.example.healthkeeper.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.healthkeeper.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CommonConn {
   /* private final String TAG = "CommonConn";
    private String url;
    private HashMap<String, Object> paramMap;
    private ProgressDialog dialog;
    private Context context;

    public CommonConn(String url,  Context context) {
        this.url = url;
        this.paramMap = new HashMap<>();
        this.context = context;
    }

    public CommonConn addParamMap(String key, Object value){
        if(key ==null) return this;
        paramMap.put(key,value);
        return this;
    }

    private void onPreExcute() {
        if (context != null && dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setProgress(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle(context.getString(R.string.app_name));
            dialog.setMessage("현재 데이터 로딩중");
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public void onExcute(appCallBack callBack){
        onPreExcute();
        CommonService service = CommonClient.getRetrofit().create(CommonService.class);

        service.clientPostMethod(url,paramMap).enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i(TAG, "onResponse: " + response.body());
                Log.i(TAG, "onResponse: " + response.errorBody());
                if(response.errorBody() ==null){
                    callBack.onResult(true, response.body());
                }else{
                    callBack.onResult(false, response.body());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                callBack.onResult(false,t.getMessage());
            }
        });
        onPostExcute();
    }

    private void onPostExcute() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();//다이얼로그 안보이게 처리
        }
    }
    public interface appCallBack{
        public void onResult(boolean isResult, String data);
    }*/


}
