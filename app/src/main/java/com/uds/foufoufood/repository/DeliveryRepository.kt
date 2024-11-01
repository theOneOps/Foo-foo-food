package com.uds.foufoufood.repository

import android.util.Log
import com.uds.foufoufood.data_class.request.AvailabilityRequest
import com.uds.foufoufood.data_class.response.AvailabilityResponse

import com.uds.foufoufood.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeliveryRepository(private val api: UserApi) {
    suspend fun updateAvailabilityOnServer(token: String, available: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    api.updateUserAvailability("Bearer $token", AvailabilityRequest(available))
                if (response.isSuccessful) {
                    Log.d(
                        "DeliveryRepository updateAvailabilityOnServer",
                        "Successfully updated availability on server"
                    )
                    true
                } else {
                    Log.e(
                        "DeliveryRepository updateAvailabilityOnServer",
                        "Failed to update availability: ${response.errorBody()?.string()}"
                    )
                    false
                }
            } catch (e: Exception) {
                Log.e(
                    "DeliveryRepository updateAvailabilityOnServer",
                    "Error updating availability on server: ${e.message}"
                )
                false
            }
        }
    }

    suspend fun getAvailabilityFromServer(token: String): Boolean? {
        val response = api.getAvailability("Bearer $token")  // Méthode GET vers l’API
        return if (response.isSuccessful) {
            val availabilityResponse: AvailabilityResponse? = response.body()
            Log.d(
                "DeliveryRepository getAvailabilityFromServer",
                "Availability response: $availabilityResponse"
            )
            availabilityResponse?.isAvailable
        } else {
            Log.e(
                "DeliveryRepository getAvailabilityFromServer",
                "Error getting availability: ${response.errorBody()?.string()}"
            )
            null
        }
    }
}