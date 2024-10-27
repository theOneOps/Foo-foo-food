package com.uds.foufoufood.network

import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.request.OrderStatusUpdateRequest
import com.uds.foufoufood.data_class.request.OrderUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApi {

    @GET("api/orders/order/{id}")
    suspend fun getOrderById(@Path("id") orderId: String): Response<Order>

    @GET("api/orders/deliveryMan/{deliveryManEmail}")
    suspend fun getOrdersByDeliveryManEmail(@Path("deliveryManEmail") deliveryManEmail: String): Response<List<Order>>

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

