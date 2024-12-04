package com.map.parkingspotter.integration.firebase.firestore.model

import com.google.firebase.firestore.DocumentId
import com.map.parkingspotter.domain.Horse
import com.map.parkingspotter.domain.HorseOverview
import com.map.parkingspotter.domain.Name

data class HorseFS(
    @DocumentId var id: String = "",
    val name: String = "",
    val age: Int = 0
) {
    fun toHorseOverview(): HorseOverview {
        return HorseOverview(id, Name(name), age.toUInt())
    }
    fun toHorseDetails(): Horse {
        return Horse(id, Name(name), age.toUInt())
    }
}