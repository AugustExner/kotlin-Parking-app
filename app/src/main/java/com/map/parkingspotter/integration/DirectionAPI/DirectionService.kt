package com.map.parkingspotter.integration.DirectionAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionService {
    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") apiKey: String
    ): Call<DirectionsResponse>
}