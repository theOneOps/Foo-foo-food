package com.uds.foufoufood.network

import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.data_class.response.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RestaurantApi {
    @GET("api/restaurants")
    suspend fun getAllRestaurants(): Response<List<Restaurant>>

    @POST("api/restaurants")
    suspend fun createRestaurant(@Body restaurant: Restaurant): Response<ApiResponse>

    @PUT("api/restaurants/{id}")
    suspend fun updateRestaurant(
        @Path("id") restaurantId: String, @Body restaurant: Restaurant
    ): Response<ApiResponse>

    @DELETE("api/restaurants/{id}")
    suspend fun deleteRestaurant(@Path("id") restaurantId: String): Response<ApiResponse>

    @PUT("api/restaurants/linkarestorer/{id}/{restaurantId}")
    suspend fun linkedARestorer(
        @Path("id") id: String, @Path("restaurantId") restaurantId: String
    ): Response<ApiResponse>
}
