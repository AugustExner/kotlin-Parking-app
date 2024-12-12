package com.map.parkingspotter.ui.components.parkingSpots

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.map.parkingspotter.ui.components.Dialog.AlertDialogExample
import kotlinx.coroutines.launch
import com.map.parkingspotter.integration.DirectionAPI.makeApiCallTestWithOriginAndDestinationParameter

@Composable
fun ParkingSpotsVejle(
    viewModel: ParkingSpotsViewModel,
    settings: String
) {
    val context = LocalContext.current
    val openAlertDialog = remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogText by remember { mutableStateOf("") }
    var selectedLatitude by remember { mutableStateOf(0.0) }
    var selectedLongitude by remember { mutableStateOf(0.0) }
    var selectedName by remember { mutableStateOf("") }

    // Fetch parking spots when the composable is first composed
    LaunchedEffect(Unit) {
        launch {
            viewModel.fetchParkingSpotsWithSettings(settings)
            makeApiCallTestWithOriginAndDestinationParameter()
        }
    }


    // Display the alert dialog when needed
    if (openAlertDialog.value) {
        AlertDialogExample(
            onDismissRequest = { openAlertDialog.value = false },

            onConfirmation = {
                openAlertDialog.value = false
                val gmmIntentUri = Uri.parse("google.navigation:q=$selectedLatitude,$selectedLongitude&mode=d")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                    setPackage("com.google.android.apps.maps")
                }
                // Check if there's an app to handle the intent
                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_SHORT).show()
                }
                println("Start Google Maps")
            },

            dialogTitle = dialogTitle,
            dialogText = dialogText,
            icon = Icons.Default.Info
        )
    }

    // Display parking spots in a lazy column
    LazyColumn {
        items(viewModel.parkingSpots) { parkingSpot ->

            Card(
                onClick = {
                    dialogTitle = "${parkingSpot.parkeringsplads} "
                    dialogText = "Would you like to start navigation to: ${parkingSpot.parkeringsplads}"
                    openAlertDialog.value = true
                    selectedLatitude = parkingSpot.latitude.toDouble()
                    selectedLongitude = parkingSpot.longitude.toDouble()
                    selectedName = parkingSpot.parkeringsplads
                },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column {
                    Log.v("Location", parkingSpot.parkeringsplads)
                    Text(text = "Settings: $settings")
                    Text(text = "Location: ${parkingSpot.parkeringsplads}")
                    Text(text = "Total Spots: ${parkingSpot.antalPladser}")
                    Text(text = "Available Spots: ${parkingSpot.ledigePladser}")
                    Text(text = "Occupied Spots: ${parkingSpot.optagedePladser}")
                    Text(text = "Price: ${parkingSpot.price}")
                    Text(text = "Distance: ${parkingSpot.distance} ")
                }
            }
        }
    }
}