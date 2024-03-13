package com.example.healthkeeper.bluetooth;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.example.healthkeeper.common.CommonConn;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class BluetoothRepository {
// 추후 조회하기 등 백그라운드 작업 추가
    private static final String TAG = BluetoothRepository.class.getSimpleName();
    private final Executor executor;
    private final CommonConn commonConn;
    public BluetoothRepository( Executor executor, Context context) {
        this.executor = executor;
        this.commonConn = new CommonConn("test/insert", context);
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