package com.map.parkingspotter.integration.DirectionAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DirectionsRetrofitClient {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    val instance: DirectionService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DirectionService::class.java)
    }
}
