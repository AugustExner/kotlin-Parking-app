package com.map.parkingspotter.ui.screen.home
//import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.map.parkingspotter.domain.Directions.Location
import com.map.parkingspotter.domain.geocoding.GeocodingViewModel
import com.map.parkingspotter.integration.firebase.auth.Service.Companion.TAG
import com.map.parkingspotter.integration.firebase.viewmodels.UserViewModel
import com.map.parkingspotter.ui.components.Dialog.AlertDialogExample
import com.map.parkingspotter.ui.components.parkingSpots.ParkingSpotsVejle
import com.map.parkingspotter.ui.components.parkingSpots.ParkingSpotsViewModel
import com.map.parkingspotter.ui.screen.home.SearchBar.DestinationSearchBar
import com.map.parkingspotter.ui.screen.home.google.GetLocations
import com.map.parkingspotter.ui.screen.home.google.MapsService
import kotlinx.coroutines.launch


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(viewModel: ParkingSpotsViewModel, userSettingsViewModel: UserViewModel, userId: String, geocodingViewModel: GeocodingViewModel) {

    val context = LocalContext.current
    val locationClient by lazy { LocationServices.getFusedLocationProviderClient(context) }
    val coroutineScope = rememberCoroutineScope()
    val setting = userSettingsViewModel.filter.value
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var destination by remember { mutableStateOf("") }
    val homeAddress by userSettingsViewModel.homeAddress
    val openAlertDialog = remember { mutableStateOf(false) }
    geocodingViewModel.getCoordinates(homeAddress)


    LaunchedEffect(userId) {
        userSettingsViewModel.loadUserSettings(userId)
    }
    LaunchedEffect(Unit) {
        viewModel.fetchParkingSpotsWithSettings(setting, destination)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            ParkingSpotsVejle(viewModel = viewModel, setting, destination)
        }
    }

    if (openAlertDialog.value) {
        AlertDialogExample(
            "Home",
            "Would you like to start navigation to: ${geocodingViewModel.address.value}" ,
            geocodingViewModel.lat.value,
            geocodingViewModel.lng.value,
            "",
            context,
            onDismiss = { openAlertDialog.value = false },
            onConfirm = {}
        )
    }

    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
            ) {
                Column() {
                    Text(
                        text = "Home Screen",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    DestinationSearchBar(onDestinationChange = { destination = it },
                        onSearch = {
                            showBottomSheet = true
                        })

                    //GoogleMaps()
                    GetLocations(MapsService(locationClient), viewModel)

                }

                SmallFloatingActionButton(
                    containerColor = Color.White,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    onClick = {
                        if (homeAddress.isNotEmpty()) {
                            openAlertDialog.value = true
                            Log.v(TAG, "Navigating to home address")
                        } else {
                            Log.v(TAG,"Home address is not set")
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 100.dp, end = 8.dp)
                        .graphicsLayer { alpha = 0.7f }
                ) {
                    Icon(
                        modifier = Modifier
                        .graphicsLayer { alpha = 1f },
                        tint = Color.DarkGray,
                        imageVector = Icons.Default.Home,
                        contentDescription = "Navigate Home"
                    )
                }

                IconButton(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    Icon(modifier = Modifier
                        .size(width = 50.dp, height = 50.dp),
                        imageVector = Icons.Default.KeyboardArrowUp,
                        tint = Color.DarkGray,
                        contentDescription = "Open Parking Spots"
                    )
                }
            }
        }
    )
}
