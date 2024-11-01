package com.uds.foufoufood.repository

import android.util.Log
import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.data_class.request.OrderRequest
import com.uds.foufoufood.data_class.request.OrderStatusUpdateRequest
import com.uds.foufoufood.data_class.request.OrderUpdateRequest
import com.uds.foufoufood.data_class.response.AssignOrderResponse
import com.uds.foufoufood.data_class.response.OrderResponse
import com.uds.foufoufood.network.OrderApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class OrderRepository(private val api: OrderApi) {
    suspend fun getOrderById(orderId: String): Response<Order> {
        return try {
            api.getOrderById(orderId)
        } catch (e: Exception) {
            Log.e("OrderRepository getOrderById", "Error getting order by ID: ${e.message}")
            throw e
        }
    }

    suspend fun getOrderByDeliverManEmail(email: String): Order? {
        return try {
            Log.d(
                "OrderRepository getOrderByDeliverManEmail",
                "Getting orders for delivery man email: $email"
            )
            val orders = api.getOrdersByDeliveryManEmail(email)
            val ordersList = orders.body() ?: emptyList()
            ordersList.firstOrNull { order -> order.status == OrderStatus.PREPARING || order.status == OrderStatus.DELIVERING }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getOrdersByDeliveryManEmail(email: String): List<Order> {
        return try {
            Log.d(
                "OrderRepository getOrdersByDeliveryManEmail",
                "Getting order for delivery man email: $email"
            )
            val response = api.getOrdersByDeliveryManEmail(email)
            if (response.isSuccessful) {
                val ordersList = response.body() ?: emptyList()
                ordersList
            } else {
                Log.e(
                    "OrderRepository getOrdersByDeliveryManEmail", "Error during order retrieval: ${
                        response.errorBody()?.string()
                    }"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(
                "OrderRepository getOrdersByDeliveryManEmail",
                "Error during order retrieval: ${e.message}"
            )
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = OrderStatusUpdateRequest(status = newStatus)
                val response = api.updateOrderStatus(orderId, request)
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun createOrder(token: String, orderRequest: OrderRequest): OrderResponse? =
        withContext(Dispatchers.IO) {
            try {
                val response = api.createOrder("Bearer $token", orderRequest)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e(
                        "OrderRepository createOrder",
                        "Failed to create order: ${response.message()}"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.e("OrderRepository createOrder", "Exception creating order: ${e.message}")
                null
            }
        }

    suspend fun hasActiveOrder(token: String, clientEmail: String): Boolean {
        return try {
            val response = api.hasActiveOrder("Bearer $token", clientEmail)
            Log.d("OrderRepository hasActiveOrder", "Has active order response: ${response.body()}")
            response.body()?.hasActiveOrder ?: false
        } catch (e: Exception) {
            Log.e("OrderRepository hasActiveOrder", "Error checking active order: ${e.message}")
            false
        }
    }

    suspend fun getCurrentOrder(token: String, clientEmail: String): Order? {
        return try {
            val response = api.getCurrentOrder("Bearer $token", clientEmail)
            if (response.isSuccessful) {
                val order = response.body()
                Log.d("OrderRepository getCurrentOrder", "Received order: $order")
                order
            } else {
                Log.e(
                    "OrderRepository getCurrentOrder",
                    "Failed to get current order: ${response.message()}"
                )
                null
            }
        } catch (e: Exception) {
            Log.e(
                "OrderRepository getCurrentOrder",
                "Exception fetching current order: ${e.message}"
            )
            null
        }
    }

    suspend fun updateOrder(updatedOrder: Order): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = OrderUpdateRequest(
                    orderId = updatedOrder.orderId,
                    status = updatedOrder.status.displayName,
                    deliveryManEmail = updatedOrder.deliveryManEmail
                )

                val response = api.updateOrder(request)

                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun assignOrderToDelivery(orderId: String): AssignOrderResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.assignOrderToDelivery(orderId)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e(
                        "OrderRepository assignOrderToDelivery",
                        "Error assigning order to delivery: ${response.message()}"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.e(
                    "OrderRepository assignOrderToDelivery",
                    "Exception assigning order to delivery: ${e.message}"
                )
                null
            }
        }
    }
}