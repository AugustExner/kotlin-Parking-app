package com.map.parkingspotter.ui.screen.home
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.map.parkingspotter.integration.vejleAPI.VejleParkingOverview
import com.map.parkingspotter.ui.components.parkingSpots.ParkingSpotsViewModel
import com.map.parkingspotter.ui.screen.home.google.CustomMarkerParkingSpot
import kotlinx.coroutines.launch

@Composable
fun GoogleMaps(
    viewModel: ParkingSpotsViewModel,
    latitude: Double,
    longitude: Double,

) {

    LaunchedEffect(Unit) {
        launch {
            viewModel.fetchParkingSpots()
        }
    }


    var parkingSpots: List<VejleParkingOverview> = viewModel.parkingSpots

    val usersPosition = LatLng(latitude, longitude)
    val usersMarkerState = rememberMarkerState(position = usersPosition)


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(usersPosition, 10f)
    }

    // Observe changes in destinationState and update the camera position
    LaunchedEffect(viewModel.destinationState) {
        if (viewModel.destinationState.lat != 0.0 && viewModel.destinationState.lng != 0.0) {
            val destination = LatLng(viewModel.destinationState.lat, viewModel.destinationState.lng)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(destination, 15f),
                durationMs = 500
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        parkingSpots.forEach { parkingSpot ->
            CustomMarkerParkingSpot(
                parkingSpot.latitude.toDouble(),
                parkingSpot.longitude.toDouble(),
                parkingSpot.parkeringsplads,
                parkingSpot.antalPladser,
                parkingSpot.ledigePladser,
                parkingSpot.price,

            )
}
        Marker(
            state = usersMarkerState,
            title = "User",
            snippet = "Marker at user location"
        )

        if (viewModel.destinationState.lat != 0.0 && viewModel.destinationState.lng != 0.0) {
            val destination = LatLng(viewModel.destinationState.lat, viewModel.destinationState.lng)
            Marker(
                state = rememberMarkerState(position = destination),
                title = viewModel.searchString,
                snippet = viewModel.destinationAdress)
        }
    }
}

