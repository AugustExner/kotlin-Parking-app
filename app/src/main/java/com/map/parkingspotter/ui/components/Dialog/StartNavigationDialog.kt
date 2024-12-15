package com.map.parkingspotter.ui.components.Dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AlertDialogExample(
    dialogTitle: String,
    dialogText: String,
    lat: Double,
    lng: Double,
    name: String,
    context: Context,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Info, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {

        },
        confirmButton = {
            TextButton(
                onClick = {
                    //ToGoogleMaps(lat, lng, context)
                    onConfirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

fun toGoogleMaps(lat: Double, lng: Double, context: Context) {
    val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng&mode=d")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }
    // Check if there's an app to handle the intent
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_SHORT).show()
    }
    println("Start Google Maps")
}