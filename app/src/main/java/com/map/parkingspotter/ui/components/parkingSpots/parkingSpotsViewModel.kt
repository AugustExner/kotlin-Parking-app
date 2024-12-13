package com.map.parkingspotter.ui.components.parkingSpots


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.map.parkingspotter.domain.Directions.Location
import com.map.parkingspotter.domain.Directions.RetrofitInstance

import com.map.parkingspotter.integration.vejleAPI.VejleParkingOverview
import com.map.parkingspotter.integration.vejleAPI.VejleRetrofitClient
import kotlinx.coroutines.launch

class ParkingSpotsViewModel : ViewModel() {
    var parkingSpots: List<VejleParkingOverview> by mutableStateOf(emptyList())
        private set

    var destinationState by mutableStateOf(Location(0.0,0.0))
    var destinationAdress by mutableStateOf("")
    var searchString by mutableStateOf("")


    fun fetchParkingSpotsWithSettings(settings: String, destination: String) {
        viewModelScope.launch {
            try {
                val response = VejleRetrofitClient.instance.getVejleParkingSpots()
                fetchDirections(response, destination)
                parkingSpots = UpdatePrices(response)
                calculatePercentage(parkingSpots)
                filterParkingSpots(settings)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchParkingSpots() {
        viewModelScope.launch {
            try {
                val response = VejleRetrofitClient.instance.getVejleParkingSpots()
                parkingSpots = UpdatePrices(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun UpdatePrices(parkingSpots: List<VejleParkingOverview>): List<VejleParkingOverview> {
        return parkingSpots.map { spot ->
            val updatedPrice = when (spot.parkeringsplads) {
                "Bryggen" -> 14
                "P-hus Cronhammar", "P-hus Albert" -> 9
                "Gunhilds Plads" -> 8
                else -> 6
            }
            spot.copy(price = updatedPrice)
        }
    }

    suspend fun fetchDirections(parkingSpots: List<VejleParkingOverview>, destination: String) {
        var hasUpdatedTarget = false
        for (spot in parkingSpots) {
            try {
                val response = RetrofitInstance.api.getDirections(
                    origin = spot.latitude + "," + spot.longitude,
                    destination = destination,
                    mode = "walking",
                    apiKey = "AIzaSyDgORILdn4tqoGRbvGsH3eKXix5LGPldi8"
                )
                // Assuming you want to display the distance and duration of the first route's first leg
                val leg = response.routes.firstOrNull()?.legs?.firstOrNull()
                Log.v("leg", "Leg: $leg")
                if (leg != null) {
                    spot.distance = leg.distance.value
                    spot.walkingTime = leg.duration.value

                    if (!hasUpdatedTarget) {
                        destinationState = Location(leg.end_location.lat, leg.end_location.lng)
                        destinationAdress = leg.end_address
                        searchString = destination
                        Log.v("direction", "Updated target: ${destinationState.lat}, ${destinationState.lng}")
                        hasUpdatedTarget = true
                    }

                } else {
                    Log.v("direction", "No directions found.")
                }
            } catch (e: Exception) {
                Log.v("direction", "Failed to fetch directions: ${e.message}")
            }
        }
    }

    fun calculatePercentage(parkingSpots: List<VejleParkingOverview>) {
        for (spot in parkingSpots) {
            val percentage = (spot.ledigePladser.toDouble() / spot.antalPladser.toDouble()) * 100
            spot.percentage = percentage.toInt()
        }
    }

    private fun filterParkingSpots(filter: String, descending: Boolean = false) {
        if (filter == "Price" && !descending) {
            parkingSpots = parkingSpots.sortedBy { it.price }
        }
        if (filter == "Available Spots" && !descending) {
            parkingSpots = parkingSpots.sortedByDescending { it.ledigePladser }
        }
    }
}