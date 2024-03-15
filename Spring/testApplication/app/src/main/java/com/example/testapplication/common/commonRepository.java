package com.example.testapplication.common;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class commonRepository {
// 추후 조회하기 등 백그라운드 작업 추가
    private static final String TAG = commonRepository.class.getSimpleName();
    private final Executor executor;
    private final CommonConn commonConn;
    public commonRepository(Executor executor, Context context) {
        this.executor = executor;
        this.commonConn = new CommonConn("test/insertAlarm", context);
    }

    public void insertData(HashMap<String, Object> map) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String json =  new Gson().toJson(map);
                commonConn.addParamMap("params",json);
                Log.d(TAG, "run: "+map);
                commonConn.onExcute((isResult, data) -> {

                    Log.d("Common", "onResult: "+data);
                    Log.d("Common", "onResult: "+isResult);

                });
    }
});
    }
}