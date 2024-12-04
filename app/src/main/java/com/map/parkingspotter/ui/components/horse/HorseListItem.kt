package com.map.parkingspotter.ui.components.horse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.map.parkingspotter.domain.HorseOverview


@Composable
fun HorseListItem(model: HorseOverview, onClick: () -> Unit) {
    Box(modifier = Modifier.clickable(onClick = onClick)) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
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
}