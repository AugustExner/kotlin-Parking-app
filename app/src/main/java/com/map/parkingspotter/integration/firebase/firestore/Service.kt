package com.map.parkingspotter.integration.firebase.firestore

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.map.parkingspotter.integration.firebase.firestore.model.HorseFS
import com.google.firebase.firestore.toObject
import com.map.parkingspotter.domain.Horse
import com.map.parkingspotter.domain.HorseOverview
import com.map.parkingspotter.domain.UserData
import com.map.parkingspotter.integration.firebase.viewmodels.UserViewModel
import kotlinx.coroutines.tasks.await

class Service {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    companion object {
        const val HORSES_COLLECTION_NAME = "Horses"
        const val USERS_COLLECTION_NAME = "users"
    }

    suspend fun getHorses(): List<HorseOverview> {
        val horses = db.collection(HORSES_COLLECTION_NAME).get().await()
        return horses.documents.mapNotNull { document ->
            document.toObject<HorseFS>()
        }.map { it.toHorseOverview() }

    }

    suspend fun getHorse(id: String): Horse? {
        return db.collection(HORSES_COLLECTION_NAME)
            .document(id)
            .get()
            .await()
            .toObject<HorseFS>()?.toHorseDetails()
    }


    suspend fun getUserSettings(id: String): UserData? {
        return try {
            val snapshot = db.collection(USERS_COLLECTION_NAME).document(id).get().await()
            if (snapshot.exists()) {
                snapshot.toObject<UserData>() // Directly deserialize into UserSettings
            } else {
                Log.e("Service", "Document not found for id: $id")
                null
            }
        } catch (e: Exception) {
            Log.e("Service", "Error fetching user settings: ${e.message}", e)
            null
        }
    }


    suspend fun updateUserSettings(userId: String, settings: UserData) {
        db.collection(USERS_COLLECTION_NAME).document(userId).set(settings).await()
    }
}