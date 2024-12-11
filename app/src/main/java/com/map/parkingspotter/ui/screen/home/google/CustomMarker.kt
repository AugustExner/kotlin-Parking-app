package com.map.parkingspotter.ui.screen.home.google

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState

@Composable
fun CustomMapBoxMarker(
    latitude: Double,
    longitude: Double,
    name: String,
    antalPladser: Int,
    ledigePladser: Int,
    price: Int,
) {
    val location = LatLng(latitude, longitude)
    val markerState = remember { MarkerState(position = location) }

    MarkerComposable(
        state = markerState,
    ) {
        OutlinedCard(
            modifier = Modifier
                .width(260.dp)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Location:",
                    )
                    Text(
                        text = name,
                        textAlign = TextAlign.End
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Available Spots:",
                    )
                    Text(
                        text = "$ledigePladser/$antalPladser",
                        textAlign = TextAlign.End
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Price:",
                    )
                    Text(
                        text = "$price DKK",
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}