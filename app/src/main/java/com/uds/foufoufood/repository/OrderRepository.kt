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
            Log.e("OrderRepository", "Erreur lors de la récupération de la commande : ${e.message}")
            throw e
        }
    }

    suspend fun getOrderByDeliverManEmail(email: String): Order? {
        return try {
            Log.d("OrderRepository", "Récupération de la commande pour l'email du livreur : $email")
            val orders = api.getOrdersByDeliveryManEmail(email)
            Log.d("OrderRepository", "Commandes récupérées : ${orders.body()}")
            // Supposons que vous avez une liste de commandes,
            // la commande si son status est PREPARING ou DELIVERING
            val ordersList = orders.body() ?: emptyList()
            ordersList.firstOrNull { order -> order.status == OrderStatus.PREPARING || order.status == OrderStatus.DELIVERING }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getOrdersByDeliveryManEmail(email: String): List<Order> {
        return try {
            Log.d("OrderRepository", "Récupération des commandes pour l'email du livreur : $email")
            val response = api.getOrdersByDeliveryManEmail(email)
            if (response.isSuccessful) {
                val ordersList = response.body() ?: emptyList()
                Log.d("OrderRepository", "Commandes récupérées : $ordersList")
                // Retourne toutes les commandes récupérées
                ordersList
            } else {
                Log.e(
                    "OrderRepository", "Erreur lors de la récupération des commandes : ${
                        response.errorBody()?.string()
                    }"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Erreur lors de la récupération des commandes : ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }


    suspend fun updateOrderStatus(orderId: String, newStatus: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Crée l'objet de requête avec le statut à mettre à jour
                val request = OrderStatusUpdateRequest(status = newStatus)

                // Effectue l'appel à l'API
                val response = api.updateOrderStatus(orderId, request)

                // Vérifie si la mise à jour a été réussie
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
                    Log.e("OrderRepository", "Failed to create order: ${response.message()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("OrderRepository", "Exception creating order: ${e.message}")
                null
            }
        }

    suspend fun hasActiveOrder(token: String, clientEmail: String): Boolean {
        return try {
            val response = api.hasActiveOrder("Bearer $token", clientEmail)
            Log.d("OrderRepository", "Has active order response: ${response.body()}")
            response.body()?.hasActiveOrder ?: false // Extract the Boolean field
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error checking active order: ${e.message}")
            false
        }
    }

    suspend fun getCurrentOrder(token: String, clientEmail: String): Order? {
        return try {
            val response = api.getCurrentOrder("Bearer $token", clientEmail)
            if (response.isSuccessful) {
                val order = response.body()
                Log.d("OrderRepository", "Received order: $order")
                order
            } else {
                Log.e("OrderRepository", "Failed to get current order: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Exception fetching current order: ${e.message}")
            null
        }
    }

    suspend fun updateOrder(updatedOrder: Order): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Crée un objet de requête pour mettre à jour la commande
                val request = OrderUpdateRequest(
                    orderId = updatedOrder.orderId,
                    status = updatedOrder.status.displayName,
                    deliveryManEmail = updatedOrder.deliveryManEmail
                )

                // Appel réel à l'API pour mettre à jour la commande
                val response = api.updateOrder(request)

                // Vérifie si la mise à jour a réussi
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
                        "OrderRepository",
                        "Erreur d'assignation de la commande: ${response.message()}"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.e(
                    "OrderRepository",
                    "Exception lors de l'assignation de la commande: ${e.message}"
                )
                null
            }
        }
    }


}

