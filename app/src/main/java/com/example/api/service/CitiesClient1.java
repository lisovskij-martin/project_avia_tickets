package com.example.api.service;

import com.example.api.model.city.City;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CitiesClient1 {
    @GET("places.json")
    Call<List<City>> reposForCities(
            @Query("term") String term,
            @Query("locale") String locale,
            @Query("types[]") String city,
            @Query("max") String max
    );
}

