package com.example.testapplication.common;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CommonRepository {
    // 추후 조회하기 등 백그라운드 작업 추가
    private static final String TAG = CommonRepository.class.getSimpleName();
    private final Executor executor;
    private CommonConn conn;
    private final Context mContext;
    //Future 또는 CompletableFuture를 사용하여 비동기 작업의 결과를 처리할 수 있습니다. 이를 사용하면 비동기 작업이 완료될 때까지 대기하거나 결과를 처리할 수 있습니다.
    private final CompletableFuture<String> result;

    public CommonRepository(Executor executor, Context context) {
        this.executor = executor;
        mContext = context;
        result = new CompletableFuture<>();
    }

    public CompletableFuture<String> insertData(String url, HashMap<String, Object> map) {
        conn = new CommonConn(url, mContext);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String json = new Gson().toJson(map);
                conn.addParamMap("params", json);
                Log.d(TAG, "run: " + map);
                conn.onExcute((isResult, data) -> {

                    Log.d("Common", "onResult: " + data);
                    Log.d("Common", "onResult: " + isResult);
                    result.complete(data);
                });
            }
        });
        return result;
    }

    public CompletableFuture<String> selectData(String url, HashMap<String, Object> map) {


        conn = new CommonConn(url, mContext);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String json = new Gson().toJson(map);
                conn.addParamMap("params", json);
                Log.d(TAG, "run: " + map);
                conn.onExcute((isResult, data) -> {

                    Log.d("Common", "onResult: " + data);
                    Log.d("Common", "onResult: " + isResult);
                    result.complete(data);
                    if (isResult)
                        result.complete(data); // 결과 완료
                });
            }
        });
        return result;
    }
}