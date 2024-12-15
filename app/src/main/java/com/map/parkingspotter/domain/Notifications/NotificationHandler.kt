package com.map.parkingspotter.domain.Notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.map.parkingspotter.R
import kotlin.random.Random

class NotificationHandler (private val context: Context){
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"


    //SIMPLE NOTIFICATION
    fun showSimpleNotification(Title: String, Text: String) {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(Title)
            .setContentText(Text)
            .setSmallIcon(R.drawable.parking_icon)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}
