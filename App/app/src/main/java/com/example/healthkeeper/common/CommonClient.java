package com.example.healthkeeper.common;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CommonClient {
    public static Retrofit getRetrofit(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.46/main/and/")
                .addConverterFactory(ScalarsConverterFactory.create()).build();
        return retrofit;
    }
}
