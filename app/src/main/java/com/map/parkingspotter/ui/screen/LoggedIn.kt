package com.map.parkingspotter.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.map.parkingspotter.domain.HorseOverview
import com.map.parkingspotter.integration.firebase.firestore.Service
import com.map.parkingspotter.ui.components.horse.HorseListItem
import com.map.parkingspotter.ui.components.menu.TabBarItem
import com.map.parkingspotter.ui.components.menu.TabView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoggedIn(onclick: (id: String) -> Unit) {
    val service = remember { Service() }
    var models by remember { mutableStateOf<List<HorseOverview>>(emptyList()) }
    val homeTab = TabBarItem(title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
    val profileTab = TabBarItem(title = "Profile", selectedIcon = Icons.Filled.Person, unselectedIcon = Icons.Outlined.Person)
    val settingsTab = TabBarItem(title = "Settings", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings)

    val tabBarItems = listOf(homeTab, profileTab, settingsTab)

    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(bottomBar = { TabView(tabBarItems, navController) }) {
            NavHost(navController = navController, startDestination = homeTab.title) {
                composable(homeTab.title) {
                    Text(homeTab.title)
                }
                composable(profileTab.title) {
                    Text(profileTab.title)
                }
                composable(settingsTab.title) {
                    Text(settingsTab.title)
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        models = service.getHorses()
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        models.forEach { HorseListItem(it) { onclick(it.id) } }
    }
}
