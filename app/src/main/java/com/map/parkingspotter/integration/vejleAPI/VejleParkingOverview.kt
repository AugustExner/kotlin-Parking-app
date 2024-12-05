package com.map.parkingspotter.integration.vejleAPI

data class VejleParkingOverview(
    val id: Int,
    val parkeringsplads: String,
    val antalPladser: Int,
    val ledigePladser: Int,
    val optagedePladser: Int,
    val latitude: String,
    val longitude: String,
    var distance: Int,
    var price: Int
)
