package com.map.parkingspotter.ui.components.parkingSpots
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.map.parkingspotter.integration.vejleAPI.VejleParkingOverview
import kotlinx.coroutines.launch


@Composable
fun ParkingSpotsVejle(
    viewModel: ParkingSpotsViewModel
) {
    // Coroutine scope for launching background tasks
    val coroutineScope = rememberCoroutineScope()

    // Fetch parking spots when the composable is first composed
    LaunchedEffect(Unit) {
        coroutineScope.launch {
                viewModel.fetchParkingSpots()
        }
    }
        // Display parking spots in a lazy column
    LazyColumn()
    {
        items(viewModel.parkingSpots) { parkingSpot ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    ), // Add border with conditional color
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                ) {
                    Text(text = "Location: ${parkingSpot.parkeringsplads}")
                    Text(text = "Total Spots: ${parkingSpot.antalPladser}")
                    Text(text = "Available Spots: ${parkingSpot.ledigePladser}")
                    Text(text = "Occupied Spots: ${parkingSpot.optagedePladser}")
                    Text(text = "Price: ${parkingSpot.price}")
                }
            }
        }
    }
}

