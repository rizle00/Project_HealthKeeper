package com.example.healthkeeper.common;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface CommonService {

    @FormUrlEncoded
    @POST
    Call<String> clientPostMethod(@Url String url, @FieldMap HashMap<String, Object> params);

    @GET("{mapping}")
    Call<String> clientGetMethod(@Path("mapping") String url, @QueryMap HashMap<String, Object> params);


}
