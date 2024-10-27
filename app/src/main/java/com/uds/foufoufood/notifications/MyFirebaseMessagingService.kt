package com.uds.foufoufood.notifications

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
        // Vérifie si la permission est accordée pour envoyer des notifications
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Crée la notification si la permission est accordée
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
}
