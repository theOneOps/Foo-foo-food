package com.uds.foufoufood.network

import com.uds.foufoufood.data_class.model.Order
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderApi {

    @GET("api/orders/order/{id}")
    suspend fun getOrderById(@Path("id") orderId: String): Response<Order>

    @GET("api/orders/deliveryMan/{deliveryManEmail}")
    suspend fun getOrdersByDeliveryManEmail(@Path("deliveryManEmail") deliveryManEmail: String): Response<List<Order>>

}