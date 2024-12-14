package com.map.parkingspotter.ui.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.google.firebase.auth.userProfileChangeRequest
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import com.map.parkingspotter.domain.geocoding.GeocodingViewModel
import com.map.parkingspotter.integration.firebase.viewmodels.UserViewModel


@Composable
fun ProfileScreen(userSettingsViewModel: UserViewModel, userId: String, geocodingViewModel: GeocodingViewModel) {
    val user = FirebaseAuth.getInstance().currentUser

    var displayName by remember { mutableStateOf(user?.displayName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var homeAddress by remember { mutableStateOf("") } // Home address state
    var isEditing by remember { mutableStateOf(false) }
    var updatedName by remember { mutableStateOf(displayName) }
    var updatedAddress by remember { mutableStateOf(homeAddress) } // Updated address state

    homeAddress = userSettingsViewModel.homeAddress.value
    displayName = userSettingsViewModel.displayName.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            enabled = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = if (isEditing) updatedName else displayName,
            onValueChange = { updatedName = it },
            label = { Text("Name") },
            enabled = isEditing
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = if (isEditing) updatedAddress else homeAddress,
            onValueChange = { updatedAddress = it },
            label = { Text("Home Address") },
            enabled = isEditing
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditing) {
            Button(onClick = {
                user?.updateProfile(userProfileChangeRequest {
                    displayName = updatedName
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        displayName = updatedName
                        homeAddress = updatedAddress
                        isEditing = false
                    }
                }
                userSettingsViewModel.updateName(updatedName)
                userSettingsViewModel.updateAddress(updatedAddress)
                geocodingViewModel.getCoordinates(updatedAddress)
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Button background color
                    contentColor = MaterialTheme.colorScheme.onSecondary  // Button text color
                )
                ) {
                Text("Save")
            }
        } else {
            Button(onClick = { isEditing = true })
            {


                Text("Edit")
            }
        }
    }
}

