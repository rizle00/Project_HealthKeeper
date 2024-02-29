package bluetooth;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import common.CommonClient;
import common.CommonConn;
import common.CommonService;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class BluetoothRepository {

    private final Handler resultHandler;
    private final Executor executor;
    private final CommonConn commonConn;
    public BluetoothRepository(Handler resultHandler, Executor executor, Context context) {
        this.resultHandler = resultHandler;
        this.executor = executor;
        this.commonConn = new CommonConn("test/insert", context);
    }
    public void insertData(HashMap<String, Object> map, RepositoryCallback<String> callback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                commonConn.pushParamMap(map);
                commonConn.onExcute((isResult, data) -> {

                    Log.d("Common", "onResult: "+data);
                    Log.d("Common", "onResult: "+isResult);
                    Result<String > result = new Result.Success<>(data);
                        notifyResult(result, callback);

                });

//                try {
//                    int num = 0;
//                    for(int i = 0; i<100; i++){
//                        num++;
//                        Result<Integer> result = new Result.Success<>(num);
//                        notifyResult(result, callback);
//                        Thread.sleep(100);
//                    }
//                    Result.Success<Integer> result = new Result.Success<>(num);
//                    result.isFinished = true;
//                    notifyResult(result, callback);
//
//                } catch (Exception e) {
//                    Result<Integer> result = new Result.Error<>(e);
//                    notifyResult(result, callback);
//                }

            }
        });
    }

    private void notifyResult(
            final Result<String> result,
            final RepositoryCallback<String > callback
    ) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });
    }
}
