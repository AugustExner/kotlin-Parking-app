package com.map.parkingspotter.integration.vejleAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object VejleRetrofitClient {
    private const val BASE_URL = "https://letparkeringapi.azurewebsites.net/"

    val instance: VejleAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VejleAPIService::class.java)
    }
}