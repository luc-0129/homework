package com.example.myapplication.api;

import com.example.myapplication.model.ApiRequest;
import com.example.myapplication.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DeepSeekApiService {
    @POST("v1/chat/completions")
    Call<ApiResponse> getCompletion(
            @Header("Authorization") String authHeader,
            @Header("Content-Type") String contentType, // 显式声明Content-Type
            @Body ApiRequest request
    );
}