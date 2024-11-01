package com.uds.foufoufood.repository

import android.util.Log
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.data_class.response.ApiResponse
import com.uds.foufoufood.network.RestaurantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestaurantRepository(private val restaurantApi: RestaurantApi) {
    suspend fun getAllRestaurants(): List<Restaurant>? = withContext(Dispatchers.IO) {
        try {
            val response = restaurantApi.getAllRestaurants()
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(
                    "RestaurantRepository getAllRestaurants",
                    "Error fetching restaurants: ${response.code()}"
                )
                null
            }
        } catch (e: Exception) {
            Log.e("RestaurantRepository getAllRestaurants", "Network error: ${e.message}")
            null
        }
    }

    suspend fun createRestaurant(restaurant: Restaurant): ApiResponse? =
        withContext(Dispatchers.IO) {
            try {
                val response = restaurantApi.createRestaurant(restaurant)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e(
                        "RestaurantRepository createRestaurant",
                        "Error creating restaurant: ${response.code()}"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.e("RestaurantRepository createRestaurant", "Network error: ${e.message}")
                null
            }
        }

    suspend fun updateRestaurant(id: String, restaurant: Restaurant): ApiResponse? =
        withContext(Dispatchers.IO) {
            try {
                val response = restaurantApi.updateRestaurant(id, restaurant)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e(
                        "RestaurantRepository updateRestaurant",
                        "Error updating restaurant: ${response.code()}"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.e("RestaurantRepository updateRestaurant", "Network error: ${e.message}")
                null
            }
        }

    suspend fun deleteRestaurant(id: String): ApiResponse? = withContext(Dispatchers.IO) {
        try {
            val response = restaurantApi.deleteRestaurant(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(
                    "RestaurantRepository deleteRestaurant",
                    "Error deleting restaurant: ${response.code()}"
                )
                null
            }
        } catch (e: Exception) {
            Log.e("RestaurantRepository deleteRestaurant", "Network error: ${e.message}")
            null
        }
    }

    suspend fun linkARestorer(id: String, restaurantId: String): ApiResponse? =
        withContext(Dispatchers.IO) {
            try {
                val response = restaurantApi.linkedARestorer(id, restaurantId)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e(
                        "RestaurantRepository linkARestorer",
                        "Error deleting restaurant: ${response.code()}"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.e("RestaurantRepository linkARestorer", "Network error: ${e.message}")
                null
            }
        }
}