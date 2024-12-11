package com.map.parkingspotter.navigation


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.map.parkingspotter.domain.Horse
import com.map.parkingspotter.integration.firebase.auth.Service
import com.map.parkingspotter.integration.firebase.viewmodels.UserViewModel
import com.map.parkingspotter.ui.components.horse.HorseDetailsItem
import com.map.parkingspotter.ui.components.menu.TabBarItem
import com.map.parkingspotter.ui.components.menu.TabView
import com.map.parkingspotter.ui.components.parkingSpots.ParkingSpotsVejle
import com.map.parkingspotter.ui.components.parkingSpots.ParkingSpotsViewModel
import com.map.parkingspotter.ui.screen.LoggedIn
import com.map.parkingspotter.ui.screen.auth.SignUp
import com.map.parkingspotter.ui.screen.auth.SignIn
import com.map.parkingspotter.ui.screen.home.HomeScreen
import com.map.parkingspotter.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {//1234qweQWE!  test1@net.dk
    val scope = rememberCoroutineScope()
    val service = remember { Service() }
    val userService = remember { com.map.parkingspotter.integration.firebase.firestore.Service() }
    val controller = rememberNavController()
    var isLoggedIn by remember { mutableStateOf(false) }

    var userId by remember { mutableStateOf("")}

    val userSettingsViewModel = remember {UserViewModel(userService)}

    val homeTab = TabBarItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    val profileTab = TabBarItem(
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
    val settingsTab = TabBarItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    val tabBarItems = listOf(homeTab, profileTab, settingsTab)


    LaunchedEffect(key1 = isLoggedIn) {
        if (isLoggedIn) {
            controller.navigate(homeTab.title)
        } else {
            controller.navigate("signup")
        }
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }



    Scaffold(
        bottomBar = {
            if (isLoggedIn) {
                TabView(tabBarItems = tabBarItems, navController = controller)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                controller,
                startDestination = if (isLoggedIn) homeTab.title else "signUp"
            ) {
                composable(route = "signUp") {
                    SignUp { email, password ->
                        scope.launch {
                            val res = service.signup(email, password)
                            isLoggedIn = res.isOk()
                            userId = res.user?.id ?: ""
                        }
                    }
                }
                composable(route = "signIn") {
                    SignIn { email, password ->
                        scope.launch {
                            val res = service.signIn(email, password)
                            isLoggedIn = res.isOk()
                            userId = res.user?.id ?: ""
                        }
                    }
                }

                composable(homeTab.title) {
                    HomeScreen(viewModel = ParkingSpotsViewModel(), userSettingsViewModel = userSettingsViewModel, userId)
                }
                composable(profileTab.title) {
                    Text(profileTab.title)
                }
                composable(settingsTab.title) {
                    SettingsScreen(userSettingsViewModel = userSettingsViewModel, userId, userService)
                }
            }



            if (!isLoggedIn) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(
                        text = "SignUp",
                        modifier = Modifier.clickable { controller.navigate("signUp") }
                    )
                    Text(
                        text = "SignIn",
                        modifier = Modifier.clickable { controller.navigate("signIn") }
                    )
                }
            }



        }
    }
}