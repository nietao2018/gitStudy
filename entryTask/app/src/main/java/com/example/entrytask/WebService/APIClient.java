package com.example.entrytask.WebService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    public static Retrofit getClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://live.test.shopee.co.id/api/v1/homepage/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }
}
