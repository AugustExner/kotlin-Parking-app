package com.map.parkingspotter.ui.screen.settings


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.map.parkingspotter.integration.firebase.firestore.Service
import com.map.parkingspotter.integration.firebase.viewmodels.UserViewModel
import com.map.parkingspotter.ui.components.settings.DropdownList
import com.map.parkingspotter.ui.theme.ParkingSpotterTheme

@Composable
fun SettingsScreen(userSettingsViewModel: UserViewModel, userId: String) {
    val filterSettings = listOf("Available Spots", "Price", "Espresso", "Latte", "Mocha")
    val themeSettings = listOf("Dark Mode", "Light Mode")

    val selectedFilter = userSettingsViewModel.filter.value
    val selectedTheme = userSettingsViewModel.theme.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .wrapContentWidth()
                .height(300.dp),
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Start), text = "Filter:"
                )
                DropdownList(
                    filterSettings,
                    "Filter",
                    selectedFilter,
                    onItemSelected = { newFilter ->
                        userSettingsViewModel.updateFilter(newFilter)
                    })
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.Start), text = "Theme:")
                DropdownList(themeSettings, "Theme", selectedTheme, onItemSelected = { newTheme ->
                    userSettingsViewModel.updateTheme(newTheme)
                })
            }
        }
    }
}