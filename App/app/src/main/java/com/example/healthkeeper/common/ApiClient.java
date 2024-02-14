package com.example.healthkeeper.common;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    public static Retrofit getApiClient(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.49/and/")
                .addConverterFactory(ScalarsConverterFactory.create()).build();

        return retrofit;
    }
}
