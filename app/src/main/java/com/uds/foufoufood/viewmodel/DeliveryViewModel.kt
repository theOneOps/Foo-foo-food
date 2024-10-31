package com.uds.foufoufood.viewmodel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.repository.DeliveryRepository
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import com.uds.foufoufood.repository.UserRepository
import com.uds.foufoufood.utils.Constants

class DeliveryViewModel(
    private val repository: DeliveryRepository,
    private val userRepository: UserRepository,
    private val context: Context
) : ViewModel() {

    private val _isAvailable = MutableStateFlow(false)
    val isAvailable: StateFlow<Boolean> = _isAvailable

    // Ajouter un StateFlow pour notifier lorsqu'une nouvelle commande est assignée
    private val _newOrderAssigned = MutableStateFlow<JSONObject?>(null)
    val newOrderAssigned: StateFlow<JSONObject?> = _newOrderAssigned

    val currentDeliveryManEmail = userRepository.getUserEmail()

    fun clearNewOrder() {
        _newOrderAssigned.value = null
    }

    private lateinit var socket: Socket

    init {
        refreshAvailability() // Synchronise l'état de disponibilité avec le serveur dès le démarrage
        observeNewOrders()
    }

    private fun observeNewOrders() {
        viewModelScope.launch {
            newOrderAssigned.collect { newOrder ->
                newOrder?.let {
                    val orderData = it.toString()
                    val orderJson = JSONObject(orderData)
                    orderJson.getJSONObject("order").getString("_id")

                    // Charger la commande et afficher la notification
                    sendNotification(
                        context,
                        "Nouvelle commande",
                        "Une nouvelle commande vous a été assignée."
                    )
                    // Délai avant d'ouvrir la commande ou gérer l'ouverture
                }
            }
        }
    }

    private fun connectSocketForOrders() {
        viewModelScope.launch {
            val userEmail = userRepository.getUserEmail()
            if (userEmail != null) {
                setupSocketConnection(userEmail)
            } else {
                Log.e("DeliveryViewModel", "Cannot connect socket: Email is null")
            }
        }
    }

    fun setAvailability(available: Boolean) {
        _isAvailable.value = available
        Log.d("DeliveryViewModel", "Availability set to $available")
        viewModelScope.launch {
            try {
                val token = getToken(context)
                if (token == null) {
                    Log.e("DeliveryViewModel", "Token is null")
                    return@launch
                }
                val success = repository.updateAvailabilityOnServer(token, available)
                if (success) {
                    Log.d("DeliveryViewModel", "Availability successfully updated on server")
                    if (available) {
                        Log.d("DeliveryViewModel", "Connecting socket for orders")
                        connectSocketForOrders() // Connecter le socket si le livreur est disponible
                    } else if (::socket.isInitialized) {
                        Log.d("DeliveryViewModel", "Disconnecting socket")
                        socket.disconnect() // Déconnecter le socket si le livreur devient indisponible
                    }
                } else {
                    Log.e("DeliveryViewModel", "Failed to update availability on server")
                    // Optionnel : Revenir à l'état précédent en cas d'échec
                    _isAvailable.value = false
                }
            } catch (e: Exception) {
                Log.e("DeliveryViewModel", "Error updating availability: ${e.message}")
                // Optionnel : Revenir à l'état précédent en cas d'erreur
                _isAvailable.value = false
            }
        }
    }

    fun resetAvailability() {
        _isAvailable.value = false
    }


    private fun setupSocketConnection(token: String) {
        try {
            socket = IO.socket(Constants.BASE_IP_ADDRESS)
            socket.connect()

            socket.emit("join", token)
            Log.d("DeliveryViewModel", "Socket connection established")

            socket.on("orderAssigned") { args ->
                if (args != null && args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    Log.d("DeliveryViewModel", "New order assigned: $data")
                    viewModelScope.launch {
                        _newOrderAssigned.value = data
                        Log.d("DeliveryViewModel", "will change screen")
                    }

                }
            }
        } catch (e: Exception) {
            Log.e("DeliveryViewModel", "Error setting up socket connection: ${e.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::socket.isInitialized) {
            socket.disconnect() // Déconnecter le socket lorsque le ViewModel est détruit
        }
    }


    fun refreshAvailability() {
        viewModelScope.launch {
            val token = getToken(context)
            if (token != null) {
                try {
                    val serverAvailability = repository.getAvailabilityFromServer(token)
                    Log.d("DeliveryViewModel", "Disponibilité récupérée: $serverAvailability")
                    serverAvailability?.let {
                        _isAvailable.value = it
                        Log.d(
                            "DeliveryViewModel",
                            "Disponibilité après mise à jour: ${_isAvailable.value}"
                        )
                    }
                } catch (e: Exception) {
                    Log.e(
                        "DeliveryViewModel",
                        "Erreur lors de la récupération de la disponibilité: ${e.message}"
                    )
                }
            }
        }
    }

    private fun sendNotification(context: Context, title: String, message: String) {
        if (ActivityCompat.checkSelfPermission(
                context, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("sendNotification", "Permission de notification accordée")

            val channelId = "order_notifications"
            val channelName = "Order Notifications"
            val channelDescription = "Notifications for new orders"

            // Create the notification channel if it doesn't already exist
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                // Check if the channel already exists
                var channel = notificationManager.getNotificationChannel(channelId)
                if (channel == null) {
                    channel = NotificationChannel(channelId, channelName, importance).apply {
                        description = channelDescription
                    }
                    notificationManager.createNotificationChannel(channel)
                    Log.d("sendNotification", "Notification channel created")
                } else {
                    Log.d("sendNotification", "Notification channel already exists")
                }
            }

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.full_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            NotificationManagerCompat.from(context)
                .notify(System.currentTimeMillis().toInt(), builder.build())
        } else {
            Log.d("sendNotification", "Permission de notification non accordée")
        }
    }



}