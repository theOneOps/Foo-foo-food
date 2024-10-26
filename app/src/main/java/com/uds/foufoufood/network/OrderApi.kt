package com.uds.foufoufood.network

import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.request.OrderRequest
import com.uds.foufoufood.data_class.response.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApi {

    @GET("api/orders/order/{id}")
    suspend fun getOrderById(@Path("id") orderId: String): Response<Order>

    @GET("api/orders/deliveryMan/{deliveryManEmail}")
    suspend fun getOrdersByDeliveryManEmail(@Path("deliveryManEmail") deliveryManEmail: String): Response<List<Order>>

    @POST("api/orders/checkout")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>

}