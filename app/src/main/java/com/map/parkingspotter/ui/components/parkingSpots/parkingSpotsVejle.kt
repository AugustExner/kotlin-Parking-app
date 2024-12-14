package com.map.parkingspotter.ui.components.parkingSpots

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.map.parkingspotter.ui.components.Dialog.AlertDialogExample
import kotlinx.coroutines.launch

@Composable
fun ParkingSpotsVejle(
    viewModel: ParkingSpotsViewModel,
    settings: String,
    destination: String
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
            viewModel.fetchParkingSpotsWithSettings(settings, destination)
        }
    }

    if (openAlertDialog.value) {
        AlertDialogExample(
            dialogTitle,
            dialogText,
            selectedLatitude,
            selectedLongitude,
            selectedName,
            context,
            onDismiss = { openAlertDialog.value = false }
        )
    }


    // Display parking spots in a lazy column
    LazyColumn {
        items(viewModel.parkingSpots) { parkingSpot ->
            val borderColor = if (parkingSpot.percentage < 10) Color.Red else Color.White

            Card(
                onClick = {
                    dialogTitle = "${parkingSpot.parkeringsplads} "
                    dialogText =
                        "Would you like to start navigation to: ${parkingSpot.parkeringsplads}"
                    selectedLatitude = parkingSpot.latitude.toDouble()
                    selectedLongitude = parkingSpot.longitude.toDouble()
                    selectedName = parkingSpot.parkeringsplads
                    openAlertDialog.value = true

                },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
                {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = "Location Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = destination.replaceFirstChar { it.uppercase() },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Text(
                            text = "${parkingSpot.distance} m" + " | " + "${parkingSpot.walkingTime} min",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))


                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = "Location Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = parkingSpot.parkeringsplads,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Ledige ${parkingSpot.ledigePladser} " + " | " + " Antal ${parkingSpot.antalPladser}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = parkingSpot.price.toString() + ".kr pr time",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }


//                    Log.v("Location", parkingSpot.parkeringsplads)
//                    Text(text = "Location: ${parkingSpot.parkeringsplads}")
//                    Text(text = "Total Spots: ${parkingSpot.antalPladser}")
//                    Text(text = "Available Spots: ${parkingSpot.ledigePladser}")
//                    Text(text = "Occupied Spots: ${parkingSpot.optagedePladser}")
//                    Text(text = "Price: ${parkingSpot.price}")
//                    Text(text = "Distance: ${parkingSpot.distance} ")
//                    Text(text = "Walking: ${parkingSpot.walkingTime / 60} minutes")
//                    Text(text = "Availability: ${parkingSpot.percentage}%")
                }
            }
        }
    }
}