package com.map.parkingspotter.ui.screen.settings


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.map.parkingspotter.integration.firebase.firestore.Service
import com.map.parkingspotter.integration.firebase.viewmodels.UserViewModel
import com.map.parkingspotter.ui.components.settings.DropdownList


@Composable
fun SettingsScreen(userSettingsViewModel: UserViewModel, userId: String, service: Service) {
    val filterSettings = listOf("Americano", "Cappuccino", "Espresso", "Latte", "Mocha")
    val themeSettings = listOf("Dark Mode", "Light Mode")

    val selectedFilter = userSettingsViewModel.filter.value
    val selectedTheme = userSettingsViewModel.theme.value

    LaunchedEffect(userId) {
        userSettingsViewModel.loadUserSettings(userId)
    }

    Column{
        Text("Settings")
        Row {
            Text("Filter:")
            DropdownList(filterSettings, "Filter", selectedFilter, onItemSelected = { newFilter ->
                userSettingsViewModel.updateFilter(newFilter)})
        }
        Row {
            Text("Theme:")
            DropdownList(themeSettings, "Theme", selectedTheme, onItemSelected = { newTheme ->
                userSettingsViewModel.updateTheme(newTheme)})
        }
    }
}