package com.uds.foufoufood.repository

import android.content.Context
import android.util.Log
import com.uds.foufoufood.network.AvailabilityRequest
import com.uds.foufoufood.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeliveryRepository(private val api: UserApi, private val context: Context) {
    suspend fun updateAvailabilityOnServer(token: String, available: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateUserAvailability("Bearer $token", AvailabilityRequest(available))
                if (response.isSuccessful) {
                    Log.d("DeliveryRepository", "Successfully updated availability on server")
                    true
                } else {
                    Log.e("DeliveryRepository", "Failed to update availability: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("DeliveryRepository", "Error updating availability on server: ${e.message}")
                false
            }
        }
    }
}