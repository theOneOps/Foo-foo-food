package com.uds.foufoufood.firebase_management

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uds.foufoufood.R

class FcmNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            showNotification(it.title ?: "Notification", it.body ?: "")
        }
    }

    private fun showNotification(title: String, message: String) {
        // Vérifie si l'application a la permission d'envoyer des notifications
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("FcmNotificationService", "Permission de notification accordée")

            val channelId = "client_order_notifications"
            val channelName = "Client Order Notifications"
            val channelDescription = "Notifications for client order updates"

            // Crée le canal de notification si nécessaire
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = getSystemService(NotificationManager::class.java)
                var channel = notificationManager.getNotificationChannel(channelId)

                if (channel == null) {
                    channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                        description = channelDescription
                    }
                    notificationManager.createNotificationChannel(channel)
                    Log.d("FcmNotificationService", "Canal de notification créé")
                } else {
                    Log.d("FcmNotificationService", "Canal de notification déjà existant")
                }
            }

            // Construit la notification
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo_only)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            // Affiche la notification avec un ID unique
            NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        } else {
            Log.d("FcmNotificationService", "Permission de notification non accordée")
        }
    }
}
