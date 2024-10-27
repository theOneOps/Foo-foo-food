package com.uds.foufoufood.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: OrderRepository,
    private val context: Context
) : ViewModel() {

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
                    _currentOrder.value = response.body()
                } else {
                    Log.e("OrderViewModel", "Erreur lors de la récupération de la commande : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Erreur lors de la récupération de la commande : ${e.message}")
            }
        }
    }

    // Fonction pour charger les commandes d'un livreur spécifique
    fun fetchOrdersForDeliveryMan(email: String) {
        viewModelScope.launch {
            try {
                val orders = repository.getOrdersByDeliveryManEmail(email)
                _orders.value = orders.sortedByDescending { it.orderId } // Tri par ordre décroissant
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Erreur lors de la récupération des commandes : ${e.message}")
            }
        }
    }

    // Fonction pour définir la commande courante (utile pour naviguer vers la page de détail)
    fun setCurrentOrder(order: Order) {
        _currentOrder.value = order
    }

    // Fonction pour charger une commande associée à un livreur par email
    fun loadOrderByDeliverManEmail(email: String) {
        viewModelScope.launch {
            _currentOrder.value = repository.getOrderByDeliverManEmail(email)
        }
    }

    // Fonction pour mettre à jour le statut de la commande
    fun updateOrderStatus(newStatus: OrderStatus) {
        _currentOrder.value?.let { order ->
            viewModelScope.launch {
                val success = repository.updateOrderStatus(order.orderId, newStatus.displayName)
                if (success) {
                    _currentOrder.value = order.copy(status = newStatus)
                } else {
                    Log.e("OrderViewModel", "Erreur lors de la mise à jour du statut de la commande.")
                }
            }
        }
    }

    // Fonction pour annuler la commande et rediriger vers la page de disponibilité
    fun cancelOrder(navController: NavHostController, deliveryViewModel: DeliveryViewModel) {
        _currentOrder.value?.let { order ->
            if (order.status != OrderStatus.WAITING) {
                viewModelScope.launch {
                    val updatedOrder = order.copy(
                        status = OrderStatus.WAITING,
                        deliveryManEmail = null
                    )
                    val success = repository.updateOrder(updatedOrder)

                    if (success) {
                        _currentOrder.value = null
                        deliveryViewModel.resetAvailability()
                        deliveryViewModel.clearNewOrder()

                        Log.d("OrderViewModel", "Redirection vers la page de disponibilité")
                        navController.navigate(Screen.DeliveryAllOrdersPage.route) {
                            popUpTo(Screen.DeliveryAllOrdersPage.route) {
                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        Log.e("OrderViewModel", "Erreur lors de la mise à jour de la commande.")
                    }
                }
            }
        }
    }
}

