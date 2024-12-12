package com.map.parkingspotter.ui.screen.home
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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