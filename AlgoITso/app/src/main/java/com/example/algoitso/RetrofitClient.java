package com.example.algoitso;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // 이부분 고칠 것 ***********************************************************
    private final static String BASE_URL = "http://ec2-18-223-44-123.us-east-2.compute.amazonaws.com:3000";
    // 이부분 고칠 것 ***********************************************************
    private static Retrofit retrofit = null;

    private RetrofitClient() {
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
