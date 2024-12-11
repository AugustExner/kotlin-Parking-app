
package com.map.parkingspotter.ui.screen.home.google
import android.location.Location
import android.location.LocationRequest
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await

class MapsService(private val locationProvider: FusedLocationProviderClient) {

    suspend fun getCurrentLocation(): Location? {
        try {
            val res =
                locationProvider.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, null)
                    .await()
            Log.v(this::class.qualifiedName, res.toString())
            return res
        } catch (ex: SecurityException) {
            Log.v(this::class.qualifiedName, ex.message.toString())
            return null
        }
    }
}