package com.map.parkingspotter.integration.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.map.parkingspotter.integration.firebase.firestore.model.HorseFS
import com.google.firebase.firestore.toObject
import com.map.parkingspotter.domain.Horse
import com.map.parkingspotter.domain.HorseOverview
import kotlinx.coroutines.tasks.await

class Service {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        const val HORSES_COLLECTION_NAME = "Horses"
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
}