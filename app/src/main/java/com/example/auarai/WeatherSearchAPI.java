package com.example.auarai;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherSearchAPI {
    @GET("geo/1.0/direct")
    Call<ArrayList<CityItemData>> getData(
            @Query("q") String q,
            @Query("limit") Integer limit,
            @Query("appid") String appid
    );
}
