package com.lokate.app.api;

import com.lokate.app.models.Group;
import com.lokate.app.models.LoginRequest;
import com.lokate.app.models.LoginResponse;
import com.lokate.app.models.OtpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    
    @POST("sendotp/")
    Call<Void> sendOtp(@Body OtpRequest request);
    
    @POST("verifyotp/")
    Call<LoginResponse> verifyOtp(@Body LoginRequest request);
    
    @GET("groups/")
    Call<List<Group>> getGroups(@Header("Authorization") String token);
}
