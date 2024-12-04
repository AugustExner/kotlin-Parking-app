package com.map.parkingspotter.ui.components.horse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.map.parkingspotter.domain.Horse
import com.map.parkingspotter.domain.HorseOverview


@Composable
fun HorseDetailsItem(model: Horse) {

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text("These are details")
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Name:")
            Text(model.name.value)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Age:")
            Text("${model.age} year(s)")
        }
    }
}