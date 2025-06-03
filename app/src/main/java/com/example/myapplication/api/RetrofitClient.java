package com.example.myapplication.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.deepseek.com/";
    private static Retrofit retrofit;

    public static DeepSeekApiService getApiService() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS) // 连接超时120秒
                    .readTimeout(120, TimeUnit.SECONDS)    // 读取超时120秒
                    .writeTimeout(120, TimeUnit.SECONDS)   // 写入超时120秒
                    .retryOnConnectionFailure(true)       // 自动重试
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(DeepSeekApiService.class);
    }
}