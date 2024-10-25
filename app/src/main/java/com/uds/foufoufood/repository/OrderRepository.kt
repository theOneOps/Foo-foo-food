package com.uds.foufoufood.repository

import android.content.Context
import android.util.Log
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.network.OrderApi
import com.uds.foufoufood.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class OrderRepository(private val api: OrderApi, private val context: Context) {
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

    // Simule la mise à jour du statut de la commande
    suspend fun updateOrderStatus(order: Order): Boolean {
        return withContext(Dispatchers.IO) {
            // Code pour mettre à jour la commande via une API REST
            // return api.updateOrderStatus(order)
            true // Simule une mise à jour réussie
        }
    }
}

