package com.uds.foufoufood.repository

import android.content.Context
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(private val api: UserApi, private val context: Context) {
    // Simule la récupération d'une commande à partir d'un ID
    suspend fun getOrderById(orderId: String): Order? {
        return withContext(Dispatchers.IO) {
            // Code pour récupérer la commande à partir de la base de données ou d'une API REST
            // return api.getOrderById(orderId)
            // Simule une commande
            Order(
                orderId = orderId,
                restaurantName = "Le Gourmet",
                restaurantAddress = Address(11, "Rue de la Paix", "Paris"),
                items = listOf(
                    Pair(Menu("1","Pizza Margherita", "Tomate, Mozzarella",13.0,"Pizza", "", "test"), 2),
                    Pair(Menu("2","Pizza Regina", "Tomate, Mozzarella, Jambon, Champignons", 15.0, "Pizza", "", "test"),1)
                ),
                clientEmail = "roussel.aymeric@gmail.com",
                clientName = "Aymeric Roussel",
                deliveryAddress = Address(15, "Rue de la Liberté", "Paris"),
                status = OrderStatus.WAITING
            )
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
