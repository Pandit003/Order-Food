package com.example.orderfood

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @POST("send-otp")
    fun sendOtp(
        @Query("client_id") clientId: String,
        @Query("phone") phone: String
    ): Call<JsonObject>

    @POST("verify-otp")
    fun verifyOtp(
        @Query("client_id") clientId: String,
        @Query("phone") phone: String,
        @Query("otp") otp: String
    ): Call<JsonObject>

    @Headers("Accept: application/json")
    @GET("user/info")
    fun getUserInfo(
        @Query("access_token") accessToken: String,
        @Query("client_id") clientId: String
    ): Call<JsonObject>

}