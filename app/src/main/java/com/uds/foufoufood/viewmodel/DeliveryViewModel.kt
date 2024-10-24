package com.uds.foufoufood.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.repository.DeliveryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeliveryViewModel(private val repository: DeliveryRepository, private val context: Context) : ViewModel() {
    private val _isAvailable = MutableStateFlow(false)
    val isAvailable: StateFlow<Boolean> = _isAvailable

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
}