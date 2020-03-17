package com.example.api.service;

import com.example.api.model.City;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CitiesClient {
    @GET("places.json")
    Call<List<City>> reposForCities(
            @Query("term") String term,
            @Query("locale") String locale,
            @Query("types[]") String city,
            @Query("max") String max
    );
}

