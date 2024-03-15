package com.example.testapplication.common;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;

public interface CommonService {

    @FormUrlEncoded
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @POST
    Call<String> clientPostMethod(@Url String url, @FieldMap HashMap<String, Object> params);

    @GET("{mapping}")
    Call<String> clientGetMethod(@Path("mapping") String url, @QueryMap HashMap<String, Object> params);


}
