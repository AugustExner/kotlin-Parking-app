package com.map.parkingspotter.integration.firebase.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.map.parkingspotter.domain.UserData
import com.map.parkingspotter.integration.firebase.firestore.Service
import kotlinx.coroutines.launch

class UserViewModel(private val service: Service) : ViewModel() {
    private val _filter = mutableStateOf("")
    val filter: MutableState<String> get() = _filter

    private val _theme = mutableStateOf("")
    val theme: MutableState<String> get() = _theme

    // Keeps track of the current user ID for Firestore updates
    private var userId: String? = null

    // Load user settings from Firestore
    fun loadUserSettings(userId: String) {
        this.userId = userId // Save userId for later updates
        viewModelScope.launch {
            val settings = service.getUserSettings(userId)
            settings?.let {
                _filter.value = it.filter
                _theme.value = it.theme
            }
        }
    }

    // Update theme and Firestore
    fun updateTheme(newTheme: String) {
        _theme.value = newTheme
        saveSettingsToFirestore()
    }

    // Update filter and Firestore
    fun updateFilter(newFilter: String) {
        _filter.value = newFilter
        saveSettingsToFirestore()
    }

    // Save updated settings to Firestore
    private fun saveSettingsToFirestore() {
        val currentUserId = userId ?: return
        val settings = UserData(
            filter = _filter.value,
            theme = _theme.value
        )
        viewModelScope.launch {
            try {
                service.updateUserSettings(currentUserId, settings)
                Log.d("UserViewModel", "Settings updated successfully")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to update settings", e)
            }
        }
    }
}
