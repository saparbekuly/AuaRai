package com.example.auarai;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherLocationAPI {
    @GET("data/2.5/weather")
    Call<WeatherLocationData> getData(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units,
            @Query("appid") String appid
    );
}