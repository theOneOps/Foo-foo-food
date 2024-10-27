package com.uds.foufoufood.network

import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.request.OrderStatusUpdateRequest
import com.uds.foufoufood.data_class.request.OrderUpdateRequest
import com.uds.foufoufood.data_class.request.OrderRequest
import com.uds.foufoufood.data_class.response.HasActiveOrderResponse
import com.uds.foufoufood.data_class.response.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
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

    @GET("api/orders/hasActiveOrder/{clientEmail}")
    suspend fun hasActiveOrder(
        @Header("Authorization") token: String,
        @Path("clientEmail") clientEmail: String
    ): Response<HasActiveOrderResponse>
    @PUT("api/orders/order/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") orderId: String,
        @Body statusRequest: OrderStatusUpdateRequest
    ): Response<Order>

    @PUT("api/orders/order/update")
    suspend fun updateOrder(
        @Body orderUpdateRequest: OrderUpdateRequest
    ): Response<Order>

}