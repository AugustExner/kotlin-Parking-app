package com.map.parkingspotter.integration.vejleAPI

import com.google.type.LatLng

data class VejleParkingOverview(
    val id: Int,
    val parkeringsplads: String,
    val antalPladser: Int,
    val ledigePladser: Int,
    val optagedePladser: Int,
    val latitude: String,
    val longitude: String,
    var distance: Int,
    var walkingTime: Int,
    var price: Int,
    var percentage: Int,
)
