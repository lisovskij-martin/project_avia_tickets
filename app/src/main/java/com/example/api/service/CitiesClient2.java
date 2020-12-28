package com.example.api.service;

import com.example.api.responseobjects.CityRO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CitiesClient2 {
    @GET("cities")
    Call<CityRO> reposForCities(
            @Query("iata") String iata
    );
}

