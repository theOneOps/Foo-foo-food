package com.uds.foufoufood.viewmodel

import android.content.Context
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

    // Fonction pour charger la commande depuis un ID
    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            val order = repository.getOrderById(orderId)
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
