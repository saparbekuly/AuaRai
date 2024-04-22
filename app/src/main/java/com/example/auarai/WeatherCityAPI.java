package com.example.auarai;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherCityAPI {
    @GET("data/2.5/weather")
    Call<WeatherLocationData> getData(
            @Query("q") String q,
            @Query("units") String units,
            @Query("appid") String appid
    );
}
