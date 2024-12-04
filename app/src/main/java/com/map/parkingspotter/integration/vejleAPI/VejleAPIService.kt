package com.map.parkingspotter.integration.vejleAPI

import retrofit2.http.GET

interface VejleAPIService {
    @GET("api/ParkingOverview")
    suspend fun getVejleParkingSpots(): List<VejleParkingOverview>
}
