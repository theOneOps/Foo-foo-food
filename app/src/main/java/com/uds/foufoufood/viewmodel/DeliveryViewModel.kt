package com.uds.foufoufood.viewmodel

import UserRepository
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.repository.DeliveryRepository
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

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

    private lateinit var socket: Socket

    fun connectSocketForOrders() {
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
                if (token == null){
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
    private fun setupSocketConnection(token: String) {
        try {
            socket = IO.socket("http://10.0.2.2:3000")
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
}