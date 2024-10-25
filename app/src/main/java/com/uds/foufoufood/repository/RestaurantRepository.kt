package com.uds.foufoufood.repository

import android.util.Log
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.data_class.response.ApiResponse
import com.uds.foufoufood.network.RestaurantApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestaurantRepository(private val restaurantApi: RestaurantApi) {

    // Fetch all restaurants
    suspend fun getAllRestaurants(): List<Restaurant>? = withContext(Dispatchers.IO) {
        try {
            val response = restaurantApi.getAllRestaurants()
            if (response.isSuccessful) {
                response.body()  // Return the list of restaurants
            } else {
                Log.e("RestaurantRepository", "Error fetching restaurants: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RestaurantRepository", "Network error: ${e.message}")
            null
        }
    }

    // Fetch a restaurant by ID
    suspend fun getRestaurantById(id: String): Restaurant? = withContext(Dispatchers.IO) {
        try {
            val response = restaurantApi.getRestaurantById(id)
            if (response.isSuccessful) {
                response.body()  // Return the restaurant details
            } else {
                Log.e("RestaurantRepository", "Error fetching restaurant: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RestaurantRepository", "Network error: ${e.message}")
            null
        }
    }

    // Create a new restaurant
    suspend fun createRestaurant(restaurant: Restaurant): ApiResponse? =
        withContext(Dispatchers.IO) {
            try {
                val response = restaurantApi.createRestaurant(restaurant)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("RestaurantRepository", "Error creating restaurant: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("RestaurantRepository", "Network error: ${e.message}")
                null
            }
        }

    // Update an existing restaurant by ID
    suspend fun updateRestaurant( id: String, restaurant: Restaurant): ApiResponse? =
        withContext(Dispatchers.IO) {
            try {
                val response = restaurantApi.updateRestaurant( id, restaurant)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("RestaurantRepository", "Error updating restaurant: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("RestaurantRepository", "Network error: ${e.message}")
                null
            }
        }

    // Delete a restaurant by ID
    suspend fun deleteRestaurant(id: String): ApiResponse? = withContext(Dispatchers.IO) {
        try {
            val response = restaurantApi.deleteRestaurant(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RestaurantRepository", "Error deleting restaurant: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RestaurantRepository", "Network error: ${e.message}")
            null
        }
    }
}
