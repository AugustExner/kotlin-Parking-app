package com.map.parkingspotter.ui.components.parkingSpots

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.map.parkingspotter.integration.vejleAPI.VejleParkingOverview
import com.map.parkingspotter.integration.vejleAPI.VejleRetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ParkingSpotsViewModel : ViewModel() {
    var parkingSpots: List<VejleParkingOverview> by mutableStateOf(emptyList())
        private set

    fun fetchParkingSpots() {
        viewModelScope.launch {
            try {
                val response = VejleRetrofitClient.instance.getVejleParkingSpots()
                parkingSpots = UpdatePrices(response)
                filterParkingSpots("availableSpots", descending = true)
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

    private fun filterParkingSpots(filter: String, descending: Boolean = false) {
        if(filter == "price" && !descending) {
            parkingSpots = parkingSpots.sortedBy { it.price }
        }
        else if(filter == "price" && descending) {
            parkingSpots = parkingSpots.sortedByDescending { it.price }
        }

        if(filter == "availableSpots" && !descending) {
            parkingSpots = parkingSpots.sortedBy { it.ledigePladser }
        }
        else if(filter == "availableSpots" && descending) {
            parkingSpots = parkingSpots.sortedByDescending { it.ledigePladser }
        }
    }
}
