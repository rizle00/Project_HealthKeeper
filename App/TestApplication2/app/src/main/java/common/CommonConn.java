package common;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;


import java.util.HashMap;

import com.example.testapplication2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CommonConn {

    //    재사용 : URL, Param, Method??
//    다이얼로그 : 데이터를 로딩중인경우 알려줌(조작x), Context
    private final String TAG = "CommonConn";
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
        if(key ==null) return this;//key에 Null삽입을 막음.
        paramMap.put(key,value);
        return this;//addParamMap(key,value).addParamMap..... 계속 연결됨.
    }

    public void pushParamMap(HashMap<String,Object> map){
        paramMap = map;
    }
    //    전송 실행 전 해야할 코드를 넣어줄 메소드 (onPre)
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
//        onPreExcute();
        CommonService service = CommonClient.getRetrofit().create(CommonService.class);
        Log.d(TAG, "map: "+paramMap);
        service.clientPostMethod(url,paramMap).enqueue(new Callback<String>() {

            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {

                Log.i(TAG, "onResponse: " + response.body());
                Log.i(TAG, "onResponse: " + response.errorBody());
                //옵저버 패턴 3번 호출 -> MainActivity
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
//        onPostExcute();
    }

    private void onPostExcute() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();//다이얼로그 안보이게 처리
        }
    }

    //    옵저버 패턴의 순서 1. 응답을 위한 메소드를 가진 인터페이스를 하나 만든다
    public interface appCallBack{
        // MainActivity에서 new로 생성 후 -> CommonConn - onResult()
        public void onResult(boolean isResult, String data);
    }

}
