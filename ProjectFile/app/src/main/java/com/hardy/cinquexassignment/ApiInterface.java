package com.hardy.cinquexassignment;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

@POST("login")

Call<JsonObject> register(@Query("email") String name, @Query("password") String password);

}
