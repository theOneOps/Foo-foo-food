// In NotificationViewModel.kt
package com.uds.foufoufood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uds.foufoufood.data_class.model.Notification
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class NotificationViewModel(
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>(emptyList())
    val notifications: LiveData<List<Notification>> get() = _notifications

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private var socket: Socket? = null

    init {
        setupSocket()
    }

    private fun setupSocket() {
        Log.d("TrackingSocketSetup", "Getting user email")
        val clientEmail = userViewModel.user.value?.email ?: return
        Log.d("TrackingSocketSetup", clientEmail)
        if (clientEmail == null) {
            _errorMessage.postValue("Utilisateur non connecté.")
            return
        }

        try {
            socket = IO.socket("http://192.168.185.108:3000")
            socket?.connect()
            val data = JSONObject()
            data.put("clientEmail", clientEmail)
            socket?.emit("register", data)
            socket?.on("notification") { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    val notification = Notification(
                        id = data.getString("orderId"),
                        message = data.getString("message"),
                        timestamp = data.getLong("timestamp")
                    )
                    addNotification(notification)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _errorMessage.postValue("Échec de la connexion au serveur.")
        }
    }

    private fun addNotification(notification: Notification) {
        val currentNotifications = _notifications.value?.toMutableList() ?: mutableListOf()
        currentNotifications.add(0, notification) // Add to the top of the list
        _notifications.postValue(currentNotifications)
    }

    fun markNotificationAsRead(notificationId: String) {
        val updatedNotifications = (_notifications.value ?: emptyList()).map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
        _notifications.postValue(updatedNotifications)
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        socket?.disconnect()
        socket?.off()
    }

    fun markAllNotificationsAsRead() {
        val updatedNotifications = (_notifications.value ?: emptyList()).map {
            if (!it.isRead) it.copy(isRead = true) else it
        }
        _notifications.postValue(updatedNotifications)
    }


}
