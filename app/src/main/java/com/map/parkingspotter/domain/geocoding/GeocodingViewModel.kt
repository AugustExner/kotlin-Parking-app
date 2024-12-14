package com.map.parkingspotter.domain.geocoding

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GeocodingViewModel : ViewModel() {

    private val geocodingRepository = GeocodingRepository()

    private var _lat = mutableStateOf(1.0)
    val lat: MutableState<Double> get() = _lat

    private var _lng = mutableStateOf(2.0)
    val lng: MutableState<Double> get() = _lng

    private var _address = mutableStateOf("")
    val address: MutableState<String> get() = _address

    fun getCoordinates(address: String) {
        viewModelScope.launch {
            try {
                val response = geocodingRepository.getCoordinatesFromAddress(address, apiKey = "AIzaSyDgORILdn4tqoGRbvGsH3eKXix5LGPldi8")
                response?.results?.firstOrNull()?.geometry?.location?.let {
                    _lat.value = it.lat
                    _lng.value = it.lng
                    _address.value = address
                    // Use the latitude and longitude
                } ?: run {
                    // Handle case when no geocode result is found
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}


