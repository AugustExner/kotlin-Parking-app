package com.map.parkingspotter.ui.screen.home
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.map.parkingspotter.integration.DirectionAPI.makeApiCall

import com.map.parkingspotter.integration.firebase.viewmodels.UserViewModel

import com.map.parkingspotter.ui.components.parkingSpots.ParkingSpotsVejle
import com.map.parkingspotter.ui.components.parkingSpots.ParkingSpotsViewModel
import com.map.parkingspotter.ui.screen.home.google.GetLocations
import com.map.parkingspotter.ui.screen.home.google.MapsService
import kotlinx.coroutines.launch


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(viewModel: ParkingSpotsViewModel, userSettingsViewModel: UserViewModel, userId: String) {

    val context = LocalContext.current
    val locationClient by lazy { LocationServices.getFusedLocationProviderClient(context) }
    val coroutineScope = rememberCoroutineScope()
    val setting = userSettingsViewModel.filter.value
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        userSettingsViewModel.loadUserSettings(userId)
    }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.fetchParkingSpotsWithSettings(setting)
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            ParkingSpotsVejle(viewModel = ParkingSpotsViewModel(),setting)
        }
    }

    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Main content of the screen goes here
                Text(text = "Welcome to Home Screen")

                //GoogleMaps()
                GetLocations(MapsService(locationClient), viewModel)

                Button(onClick = {

                    Log.v("Distance", "makeApiCall() -->")
                    makeApiCall() })  {

                }

                IconButton(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Open Parking Spots"
                    )
                }
            }
        }
    )


}