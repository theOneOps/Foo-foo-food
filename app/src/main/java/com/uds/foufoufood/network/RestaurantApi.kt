package com.uds.foufoufood.network

import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.data_class.response.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RestaurantApi {

    // Fetch all restaurants
    @GET("api/restaurants")
    suspend fun getAllRestaurants(): Response<List<Restaurant>>

    // Fetch a specific restaurant by ID
    @GET("api/restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") restaurantId: String): Response<Restaurant>

    // Create a new restaurant
    @POST("api/restaurants")
    suspend fun createRestaurant(@Body restaurant: Restaurant): Response<ApiResponse>

    // Update an existing restaurant by ID
    @PUT("api/restaurants/{id}")
    suspend fun updateRestaurant(
        @Path("id") restaurantId: String,
        @Body restaurant: Restaurant
    ): Response<ApiResponse>

    // Delete a restaurant by ID
    @DELETE("api/restaurants/{id}")
    suspend fun deleteRestaurant(@Path("id") restaurantId: String): Response<ApiResponse>
}
