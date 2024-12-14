package com.map.parkingspotter.ui.screen.home.google

import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.map.parkingspotter.ui.components.parkingSpots.ParkingSpotsViewModel
import com.map.parkingspotter.ui.screen.home.GoogleMaps

private const val PERMISSION = "android.permission.ACCESS_FINE_LOCATION"

@Composable
fun GetLocations(mapsService: MapsService, viewModel: ParkingSpotsViewModel) {

    val context = LocalContext.current
    val granted = remember {
        mutableStateOf(
            PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context,
                PERMISSION
            )
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        granted.value = isGranted
    }
    val currentLocation = remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(key1 = Unit) {
        if (!granted.value) {
            launcher.launch("android.permission.ACCESS_FINE_LOCATION")
        } else {
            currentLocation.value = mapsService.getCurrentLocation()
        }
    }

    if (currentLocation.value != null) {
        val location = currentLocation.value!!
        val latitude = location.latitude
        val longitude = location.longitude

        GoogleMaps(viewModel, latitude, longitude)
        //Text("Current Location: Latitude: ${latitude}, Longitude: ${longitude}")
    } else {
        //Text("Fetching current location...")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
            ) {


            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}