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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.map.parkingspotter.ui.components.Dialog.AlertDialogExample
import kotlinx.coroutines.delay
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
    var selectedPercentage by remember { mutableStateOf(0) }
    val isNavigationStarted = remember { mutableStateOf(false) }


    // Fetch parking spots when the composable is first composed
    LaunchedEffect(Unit) {
        launch {
            viewModel.fetchParkingSpotsWithSettings(settings, destination)
        }
    }

    fun startNavigation(latitude: Double, longitude: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude&mode=d")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
            Log.v("Navigation", "Navigation started")
        }
        // Check if there's an app to handle the intent
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_SHORT).show()
        }
        println("Start Google Maps")
    }


    // Display the alert dialog when needed
    if (openAlertDialog.value) {
        AlertDialogExample(
            onDismissRequest = { openAlertDialog.value = false },

            onConfirmation = {
                isNavigationStarted.value = true
                openAlertDialog.value = false
                startNavigation(selectedLatitude, selectedLongitude)
            },

            dialogTitle = dialogTitle,
            dialogText = dialogText,
            icon = Icons.Default.Info
        )
    }

    // Check if we should reroute
//    LaunchedEffect(isNavigationStarted.value) {
//        if (isNavigationStarted.value && selectedPercentage < 10) {
//            Log.v("reroute", "Reroute should begin after 5 sec")
//            delay(1000)
//            startNavigation(56.132731, 10.196614)
//
////            // Ensure parking spots list is not empty
////            if (viewModel.parkingSpots.isNotEmpty()) {
////                val firstSpot = viewModel.parkingSpots.first()
////                Log.v("reroute", "Found new spot: ${firstSpot.parkeringsplads}")
////                // Update selected spot details for potential UI feedback
////                selectedLatitude = firstSpot.latitude.toDouble()
////                selectedLongitude = firstSpot.longitude.toDouble()
////                selectedName = firstSpot.parkeringsplads
////                selectedPercentage = firstSpot.percentage
////
////                // Start navigation to the first spot
////                startNavigation(selectedLatitude, selectedLongitude)
////            } else {
////                Log.v("reroute", "No parking spots available for rerouting")
////            }
//        }
//    }



    // Display parking spots in a lazy column
    LazyColumn {
        items(viewModel.parkingSpots) { parkingSpot ->
            val borderColor = if (parkingSpot.percentage < 10) Color.Red else Color.White

            Card(
                onClick = {
                    dialogTitle = "${parkingSpot.parkeringsplads} "
                    dialogText = "Would you like to start navigation to: ${parkingSpot.parkeringsplads}"
                    openAlertDialog.value = true
                    selectedLatitude = parkingSpot.latitude.toDouble()
                    selectedLongitude = parkingSpot.longitude.toDouble()
                    selectedName = parkingSpot.parkeringsplads
                    selectedPercentage = parkingSpot.percentage
                },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1)),
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
                        .fillMaxWidth())
                {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = "Location Icon",
                                tint = Color(0xFF0D47A1),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = destination.replaceFirstChar { it.uppercase() },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "${parkingSpot.distance} m" + " | " + "${parkingSpot.walkingTime} min",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))


                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
                        ) {
                            Row {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "Location Icon",
                                    tint = Color(0xFF0D47A1),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = parkingSpot.parkeringsplads,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
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
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = parkingSpot.price.toString() + ".kr pr time",
                                    fontSize = 14.sp,
                                    color = Color.Gray
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