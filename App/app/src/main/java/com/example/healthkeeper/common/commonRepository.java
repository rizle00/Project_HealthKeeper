package com.example.healthkeeper.common;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class commonRepository {
// 추후 조회하기 등 백그라운드 작업 추가
    private static final String TAG = commonRepository.class.getSimpleName();
    private final Executor executor;
    private CommonConn conn;
    private final Context mContext;
    public commonRepository(Executor executor, Context context) {
        this.executor = executor;
        mContext = context;

    }

    public void insertData(String url,HashMap<String, Object> map) {
        conn = new CommonConn(url, mContext);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String json =  new Gson().toJson(map);
                conn.addParamMap("params",json);
                Log.d(TAG, "run: "+map);
                conn.onExcute((isResult, data) -> {

                    Log.d("Common", "onResult: "+data);
                    Log.d("Common", "onResult: "+isResult);

                });
    }
});
    }

    public void selectData(String url,HashMap<String, Object> map) {
        conn = new CommonConn(url, mContext);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String json =  new Gson().toJson(map);
                conn.addParamMap("params",json);
                Log.d(TAG, "run: "+map);
                conn.onExcute((isResult, data) -> {

                    Log.d("Common", "onResult: "+data);
                    Log.d("Common", "onResult: "+isResult);

                });
            }
        });
    }
}