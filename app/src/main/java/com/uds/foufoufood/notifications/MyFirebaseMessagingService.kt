package com.uds.foufoufood.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uds.foufoufood.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Vérifiez les données du message pour identifier l'événement (commande assignée, statut changé, etc.)
        val eventTitle = remoteMessage.data["title"] ?: "Notification"
        val eventBody = remoteMessage.data["body"] ?: "Un événement est survenu"

        // Créez une notification pour l'afficher à l'utilisateur
        sendNotification(eventTitle, eventBody)
    }

    private fun sendNotification(title: String, message: String) {
        createNotificationChannel() // Create the channel if it doesn't exist

        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val builder = NotificationCompat.Builder(this, "event_channel")
                .setSmallIcon(R.drawable.full_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            with(NotificationManagerCompat.from(this)) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "event_channel"
            val channelName = "Event Notifications"
            val channelDescription = "Notifications for events such as order status changes"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}
