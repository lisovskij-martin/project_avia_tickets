package com.example.api.service;

import com.example.api.responseobjects.TicketRO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TicketClient {
    @GET("price_matrix")
    Call<TicketRO> reposForTickets(
            @Query("origin_iata") String origin_iata,
            @Query("destination_iata") String destination_iata,
            @Query("depart_start") String depart_start,
            @Query("currency") String currency,
            @Query("depart_range") String depart_range,
            @Query("affiliate") String affiliate
    );
}


