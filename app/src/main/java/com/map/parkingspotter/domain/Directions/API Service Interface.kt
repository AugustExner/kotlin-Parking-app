package com.map.parkingspotter.domain.Directions

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApiService {

    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") apiKey: String
    ): DirectionsResponse

}
