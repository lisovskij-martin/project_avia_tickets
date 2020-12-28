package com.example.api.service;

import com.example.api.responseobjects.TripRO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TripClient {
    @GET("experiences")
    Call<TripRO> reposForTrips(
            @Query("city") String city,
            @Query("format") String format,
            @Query("detailed") String detailed,
            @Query("sorting") String sorting,
            @Query("start_price") String start_price,
            @Query("end_price") String end_price,
            @Query("start_date") String start_date,
            @Query("end_date") String end_date
    );
}


