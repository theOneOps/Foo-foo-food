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

    private val _newOrderAssigned = MutableStateFlow<JSONObject?>(null)
    val newOrderAssigned: StateFlow<JSONObject?> = _newOrderAssigned

    val currentDeliveryManEmail = userRepository.getUserEmail()

    fun clearNewOrder() {
        _newOrderAssigned.value = null
    }

    private lateinit var socket: Socket

    init {
        refreshAvailability()
        observeNewOrders()
    }

    private fun observeNewOrders() {
        viewModelScope.launch {
            newOrderAssigned.collect { newOrder ->
                newOrder?.let {
                    val orderData = it.toString()
                    val orderJson = JSONObject(orderData)
                    orderJson.getJSONObject("order").getString("_id")

                    sendNotification(
                        context,
                        "Nouvelle commande",
                        "Une nouvelle commande vous a été assignée."
                    )
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
                Log.e("DeliveryViewModel connectSocketForOrders", "Cannot connect socket: Email is null")
            }
        }
    }

    fun setAvailability(available: Boolean) {
        _isAvailable.value = available
        viewModelScope.launch {
            try {
                val token = getToken(context)
                if (token == null) {
                    return@launch
                }
                val success = repository.updateAvailabilityOnServer(token, available)
                if (success) {
                    Log.d("DeliveryViewModel setAvailability", "Availability successfully updated on server")
                    if (available) {
                        connectSocketForOrders()
                    } else if (::socket.isInitialized) {
                        socket.disconnect()
                    }
                } else {
                    _isAvailable.value = false
                }
            } catch (e: Exception) {
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
            socket.on("orderAssigned") { args ->
                if (args != null && args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    viewModelScope.launch {
                        _newOrderAssigned.value = data
                    }

                }
            }
        } catch (e: Exception) {
            Log.e("DeliveryViewModel setupSocketConnection", "Error setting up socket connection: ${e.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::socket.isInitialized) {
            socket.disconnect()
        }
    }

    fun refreshAvailability() {
        viewModelScope.launch {
            val token = getToken(context)
            if (token != null) {
                try {
                    val serverAvailability = repository.getAvailabilityFromServer(token)
                    serverAvailability?.let {
                        _isAvailable.value = it
                    }
                } catch (e: Exception) {
                    Log.e(
                        "DeliveryViewModel refreshAvailability",
                        "Error refreshing availability: ${e.message}"
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
            val channelId = "order_notifications"
            val channelName = "Order Notifications"
            val channelDescription = "Notifications for new orders"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                var channel = notificationManager.getNotificationChannel(channelId)
                if (channel == null) {
                    channel = NotificationChannel(channelId, channelName, importance).apply {
                        description = channelDescription
                    }
                    notificationManager.createNotificationChannel(channel)
                } else {
                    Log.d("DeliveryViewModel sendNotification", "Channel already exists")
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
            Log.d("DeliveryViewModel sendNotification", "No notification permission")
        }
    }
}