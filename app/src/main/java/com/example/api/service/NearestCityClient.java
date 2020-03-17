package com.example.api.service;

import com.example.api.model.City;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearestCityClient {
    @GET("nearest_places.json")
    Call<List<City>> reposForGorods(
            @Query("locale") String locale
    );
}

