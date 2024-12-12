package com.map.parkingspotter.ui.components.parkingSpots

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.map.parkingspotter.domain.Directions.RetrofitInstance
import com.map.parkingspotter.integration.DirectionAPI.makeApiCallTestWithOriginAndDestinationParameter
import com.map.parkingspotter.integration.vejleAPI.VejleParkingOverview
import com.map.parkingspotter.integration.vejleAPI.VejleRetrofitClient
import kotlinx.coroutines.launch

class ParkingSpotsViewModel : ViewModel() {
    var parkingSpots: List<VejleParkingOverview> by mutableStateOf(emptyList())
        private set

    var destination = mutableStateOf("")
        private set

    fun setDestination(newDestination: String) {
        destination.value = newDestination
    }

    fun fetchParkingSpotsWithSettings(settings: String, destination: String) {
        viewModelScope.launch {
            try {
                val response = VejleRetrofitClient.instance.getVejleParkingSpots()
                fetchDirections(response, destination)
                parkingSpots = UpdatePrices(response)
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
                if (leg != null) {
                    spot.distance = leg.distance.value
                } else {
                    Log.v("direction", "No directions found.")
                }
            } catch (e: Exception) {
                Log.v("direction", "Failed to fetch directions: ${e.message}")
            }
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
