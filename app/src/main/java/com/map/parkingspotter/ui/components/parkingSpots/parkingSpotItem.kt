package com.map.parkingspotter.ui.components.parkingSpots

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.map.parkingspotter.domain.Notifications.NotificationHandler
import com.map.parkingspotter.integration.vejleAPI.VejleParkingOverview
import com.map.parkingspotter.ui.components.Dialog.AlertDialogExample

@Composable
fun ParkingSpotItem(vejleParkingOverview: VejleParkingOverview, destination: String, viewModel: ParkingSpotsViewModel ){

    val context = LocalContext.current
    val openAlertDialog = remember { mutableStateOf(false) }
    val isGoogleMapsLaunched = remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogText by remember { mutableStateOf("") }
    var selectedLatitude by remember { mutableStateOf(0.0) }
    var selectedLongitude by remember { mutableStateOf(0.0) }
    var selectedName by remember { mutableStateOf("") }
    var currentParkingSpot = vejleParkingOverview
    val borderColor = if (currentParkingSpot.percentage < 10) Color.Red else Color.White

    val notificationHandler = NotificationHandler(context)
    val lifecycleOwner = LocalContext.current as LifecycleOwner



    fun launchGoogleMaps(lat: Double, lng: Double, context: Context) {
        isGoogleMapsLaunched.value = true
        val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng&mode=d")
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
    }

    LaunchedEffect(isGoogleMapsLaunched.value){
        if (isGoogleMapsLaunched.value){

            while (true){
                if(currentParkingSpot.percentage < 10) {
                   val newSpot = viewModel.findBestAvailableSpot()
                    if (newSpot !=null){
                        Log.v("Notify", "Notificaiton")
                        notificationHandler.showSimpleNotification("${currentParkingSpot.parkeringsplads} Is occupied"," Rerouting to ${newSpot.parkeringsplads} in 5 seconds")
                        currentParkingSpot = newSpot
                        Log.v("Notify", "Notificaiton")
                        launchGoogleMaps(newSpot.latitude.toDouble(), newSpot.longitude.toDouble(), context)
                        kotlinx.coroutines.delay(5000)
                        //launchGoogleMaps(newSpot.latitude.toDouble(), newSpot.longitude.toDouble(), context)
                    }
                }
                kotlinx.coroutines.delay(5000)
            }
        }
    }

    // Lifecycle event observer to track when the user returns to the app
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // When the app is resumed, set Google Maps as not launched
                isGoogleMapsLaunched.value = false
            }
        }
        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // Cleanup when the composable leaves the composition
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
            onDismiss = { openAlertDialog.value = false },
            onConfirm = { openAlertDialog.value = false
                launchGoogleMaps(selectedLatitude, selectedLongitude, context)
            }
        )
    }


    Card(
        onClick = {
            dialogTitle = "${currentParkingSpot.parkeringsplads} "
            dialogText =
                "Would you like to start navigation to: ${currentParkingSpot.parkeringsplads}"
            selectedLatitude = currentParkingSpot.latitude.toDouble()
            selectedLongitude = currentParkingSpot.longitude.toDouble()
            selectedName = currentParkingSpot.parkeringsplads
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
                    text = "${currentParkingSpot.distance} m" + " | " + "${currentParkingSpot.walkingTime} min",
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
                        text = currentParkingSpot.parkeringsplads,
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
                    text = "Ledige ${currentParkingSpot.ledigePladser} " + " | " + " Antal ${currentParkingSpot.antalPladser}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = currentParkingSpot.price.toString() + ".kr pr time",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }



        }
    }


}