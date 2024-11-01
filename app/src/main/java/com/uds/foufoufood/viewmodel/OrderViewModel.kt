package com.uds.foufoufood.viewmodel

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
    private val repository: OrderRepository
) : ViewModel() {

    private val _currentOrder = MutableStateFlow<Order?>(null)
    val currentOrder: StateFlow<Order?> = _currentOrder

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getOrderById(orderId)
                if (response.isSuccessful) {
                    _currentOrder.value = response.body()
                } else {
                    Log.e(
                        "OrderViewModel loadOrder", "Failed to fetch order: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "OrderViewModel loadOrder", "Failed to fetch order: ${e.message}"
                )
            }
        }
    }

    fun fetchOrdersForDeliveryMan(email: String) {
        viewModelScope.launch {
            try {
                val orders = repository.getOrdersByDeliveryManEmail(email)
                _orders.value =
                    orders.sortedByDescending { it.orderId }
            } catch (e: Exception) {
                Log.e(
                    "OrderViewModel fetchOrdersForDeliveryMan", "Failed to fetch orders: ${e.message}"
                )
            }
        }
    }

    fun setCurrentOrder(order: Order) {
        _currentOrder.value = order
    }

    fun loadOrderByDeliverManEmail(email: String) {
        viewModelScope.launch {
            _currentOrder.value = repository.getOrderByDeliverManEmail(email)
        }
    }

    fun updateOrderStatus(newStatus: OrderStatus) {
        _currentOrder.value?.let { order ->
            viewModelScope.launch {
                val success = repository.updateOrderStatus(order.orderId, newStatus.displayName)
                if (success) {
                    _currentOrder.value = order.copy(status = newStatus)
                } else {
                    Log.e(
                        "OrderViewModel updateOrderStatus", "Failed to update order status."
                    )
                }
            }
        }
    }

    fun cancelOrder(navController: NavHostController, deliveryViewModel: DeliveryViewModel) {
        _currentOrder.value?.let { order ->
            if (order.status != OrderStatus.WAITING) {
                viewModelScope.launch {
                    val updatedOrder = order.copy(
                        status = OrderStatus.WAITING, deliveryManEmail = null
                    )
                    val success = repository.updateOrder(updatedOrder)

                    if (success) {
                        _currentOrder.value = null
                        deliveryViewModel.resetAvailability()
                        deliveryViewModel.clearNewOrder()

                        navController.navigate(Screen.DeliveryAllOrdersPage.route) {
                            popUpTo(Screen.DeliveryAllOrdersPage.route) {
                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        Log.e("OrderViewModel cancelOrder", "Failed to cancel order.")
                    }
                }
            }
        }
    }
}