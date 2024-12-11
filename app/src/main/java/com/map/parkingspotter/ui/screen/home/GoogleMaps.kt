package com.map.parkingspotter.ui.screen.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.map.parkingspotter.ui.components.parkingSpots.ParkingSpotsViewModel
import com.map.parkingspotter.ui.screen.home.google.CustomMapBoxMarker
import kotlinx.coroutines.launch

@Composable
fun GoogleMaps(
    viewModel: ParkingSpotsViewModel,
    latitude: Double,
    longitude: Double
) {

    LaunchedEffect(Unit) {
        launch {
            viewModel.fetchParkingSpots()
        }
    }



    //Vejle Marker
    val vejle = LatLng(55.71018915996731, 9.535023208337762)
    val vejleMarkerState = rememberMarkerState(position = vejle)


    val usersPosition = LatLng(latitude, longitude)
    val usersMarkerState = rememberMarkerState(position = usersPosition)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(usersPosition, 10f)
    }



    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        viewModel.parkingSpots.forEach { parkingSpot ->
            CustomMapBoxMarker(
                parkingSpot.latitude.toDouble(),
                parkingSpot.longitude.toDouble(),
                parkingSpot.parkeringsplads,
                parkingSpot.antalPladser,
                parkingSpot.ledigePladser, parkingSpot.price)
}
        Marker(
            state = usersMarkerState,
            title = "User",
            snippet = "Marker at user location"
        )
    }
}