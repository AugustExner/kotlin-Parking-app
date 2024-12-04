package com.map.parkingspotter.integration.firebase.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.map.parkingspotter.integration.firebase.firestore.Service
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _filter = mutableStateOf("")
    val filter: MutableState<String> get() = _filter

    private val _theme = mutableStateOf("")
    val theme: MutableState<String> get() = _theme

    fun updateTheme(newTheme: String) {
        _theme.value = newTheme
    }

    fun updateFilter(newFilter: String) {
        _filter.value = newFilter
    }

    fun loadUserSettings(userId: String, service: Service) {
        viewModelScope.launch {
            val settings = service.getUserSettings(userId)
            settings?.let {
                filter.value = it.filter
                theme.value = it.theme
            }
        }
    }
}