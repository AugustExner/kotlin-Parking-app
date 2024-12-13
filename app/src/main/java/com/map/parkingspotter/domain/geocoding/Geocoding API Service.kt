package com.map.parkingspotter.domain.geocoding

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingAPIService {

    @GET("maps/api/geocode/json")
    suspend fun getGeocode(
        @Query("address") address: String, // Address or location to geocode
        @Query("key") apiKey: String // Google Maps API Key
    ): GeocodingResponse
}
