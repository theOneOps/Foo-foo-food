package com.uds.foufoufood.network

import com.uds.foufoufood.data_class.model.Restaurant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantApi {

    // Fetch all restaurants
    @GET("api/restaurants")
    suspend fun getAllRestaurants(): Response<List<Restaurant>>

    // Fetch a specific restaurant by ID
    @GET("api/restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") restaurantId: String): Response<Restaurant>
}
