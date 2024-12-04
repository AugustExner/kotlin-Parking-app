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
                parkingSpots = response.map {spot -> updatePrice(spot)}
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePrice(specificSpot: VejleParkingOverview): VejleParkingOverview {
        if (specificSpot.parkeringsplads == "Bryggen") {
            specificSpot.price = 14
        }
        if (specificSpot.parkeringsplads == "P-hus Cronhammar" || specificSpot.parkeringsplads == "P-hus Albert") {
            specificSpot.price = 9
        }
        if (specificSpot.parkeringsplads == "Gunhilds Plads") {
            specificSpot.price = 8
        }
        else {
            specificSpot.price = 6
        }
        return specificSpot.copy(price = specificSpot.price)
    }
}