package com.uds.foufoufood.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository, private val context: Context) : ViewModel() {


    // État de la commande active
    private val _currentOrder = MutableStateFlow<Order?>(null)
    val currentOrder: StateFlow<Order?> = _currentOrder

    // État des commandes récupérées pour le livreur
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    // Fonction pour charger la commande depuis un ID
    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getOrderById(orderId)
                if (response.isSuccessful) {
                    val order = response.body()
                    if (order != null) {
                        _currentOrder.value = order
                    } else {
                        Log.e("OrderViewModel", "Erreur : La commande récupérée est nulle.")
                    }
                } else {
                    Log.e("OrderViewModel", "Erreur lors de la récupération de la commande : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Erreur lors de la récupération de la commande : ${e.message}")
            }
        }
    }

    fun fetchOrdersForDeliveryMan(email: String) {
        viewModelScope.launch {
            try {
                val orders = repository.getOrdersByDeliveryManEmail(email)
                _orders.value = orders.sortedBy { it.orderId } // Tri par ordre décroissant
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Erreur lors de la récupération des commandes : ${e.message}")
            }
        }
    }


    // Fonction pour définir la commande courante (utile pour naviguer vers la page de détail)
    fun setCurrentOrder(order: Order) {
        _currentOrder.value = order
    }


    fun loadOrderByDeliverManEmail(email: String) {
        viewModelScope.launch {
            val order = repository.getOrderByDeliverManEmail(email)
            Log.d("OrderViewModel", "Order found: $order")
            _currentOrder.value = order
        }
    }

    // Fonction pour mettre à jour le statut de la commande
    fun updateOrderStatus(newStatus: OrderStatus) {
        _currentOrder.value?.let { order ->
            viewModelScope.launch {
                val updatedOrder = order.copy(status = newStatus)
                val success = repository.updateOrderStatus(updatedOrder)
                if (success) {
                    _currentOrder.value = updatedOrder
                }
            }
        }
    }

    // Fonction pour annuler la commande
    fun cancelOrder() {
        _currentOrder.value?.let { order ->
            if (order.status != OrderStatus.WAITING) {
                updateOrderStatus(OrderStatus.WAITING)
            }
        }
    }

}
