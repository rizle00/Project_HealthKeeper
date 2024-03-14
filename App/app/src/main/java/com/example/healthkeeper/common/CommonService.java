package com.example.healthkeeper.common;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.*;

public interface CommonService {

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")// 한글 깨짐 방지
    @POST
    Call<String> clientPostMethod(@Url String url, @FieldMap HashMap<String, Object> params);

    @GET("{mapping}")
    Call<String> clientGetMethod(@Path("mapping") String url, @QueryMap HashMap<String, Object> params);


}
