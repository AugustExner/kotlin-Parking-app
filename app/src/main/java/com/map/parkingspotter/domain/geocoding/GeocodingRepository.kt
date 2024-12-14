package com.map.parkingspotter.domain.geocoding

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeocodingRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/") // Base URL for Google Maps API
        .addConverterFactory(GsonConverterFactory.create()) // Converter for parsing JSON
        .build()

    private val apiService = retrofit.create(GeocodingAPIService::class.java)

    suspend fun getCoordinatesFromAddress(address: String, apiKey: String): GeocodingResponse? {
        return try {
            val response = apiService.getGeocode(address, apiKey)
            if (response.status == "OK") {
                response // Return the valid geocoding result
            } else {
                null // Handle any errors or no results
            }
        } catch (e: Exception) {
            // Handle exception
            null
        }
    }
}
