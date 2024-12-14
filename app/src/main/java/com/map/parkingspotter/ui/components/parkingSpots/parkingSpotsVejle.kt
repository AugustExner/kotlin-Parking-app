package com.map.parkingspotter.ui.components.parkingSpots

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@Composable
fun ParkingSpotsVejle(
    viewModel: ParkingSpotsViewModel,
    settings: String,
    destination: String
) {


    // Fetch parking spots when the composable is first composed
    LaunchedEffect(Unit) {
        launch {
            viewModel.fetchParkingSpotsWithSettings(settings, destination)
        }
    }

    // Display parking spots in a lazy column
    LazyColumn {
        items(viewModel.parkingSpots) { parkingSpot -> ParkingSpotItem(
            vejleParkingOverview = parkingSpot,
            destination = destination,
            viewModel = viewModel
        )

    }
}
}