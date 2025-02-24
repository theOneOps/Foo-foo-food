package com.uds.foufoufood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.repository.OrderRepository
import com.uds.foufoufood.utils.Constants
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class OrderTrackingViewModel(
    private val orderRepository: OrderRepository,
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val _currentOrder = MutableLiveData<Order?>()
    val currentOrder: LiveData<Order?> get() = _currentOrder

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private var socket: Socket? = null

    init {
        setupSocket()
        startPeriodicRefresh()
    }

    fun fetchCurrentOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            val token = userViewModel.token.value
            val clientEmail = userViewModel.user.value?.email

            if (token != null && clientEmail != null) {
                val order = orderRepository.getCurrentOrder(token, clientEmail)
                if (order != null) {
                    _currentOrder.postValue(order)
                } else {
                    _errorMessage.postValue("No active order found.")
                    _currentOrder.postValue(null)
                }
            } else {
                _errorMessage.postValue("User not logged in.")
            }
        }
    }

    private fun setupSocket() {
        val clientEmail = userViewModel.user.value?.email ?: return
        try {
            socket = IO.socket(Constants.BASE_IP_ADDRESS)
            socket?.connect()
            val data = JSONObject()
            data.put("clientEmail", clientEmail)
            socket?.emit("register", data)
            socket?.on("orderStatusUpdated") { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    val orderId = data.getString("orderId")
                    val status = data.getString("status")
                    updateOrderStatus(orderId, status)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _errorMessage.postValue("Failed to connect to server.")
        }
    }

    private fun updateOrderStatus(orderId: String, status: String) {
        _currentOrder.value?.let { order ->
            if (order.orderId == orderId) {
                val newStatus = OrderStatus.fromDisplayName(status)
                if (newStatus != null) {
                    val updatedOrder = order.copy(status = newStatus)
                    _currentOrder.postValue(updatedOrder)
                } else {
                    _errorMessage.postValue("Statut de commande inconnu : $status")
                }
            }
        }
    }

    fun refreshOrder() {
        fetchCurrentOrder()
    }

    private fun startPeriodicRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(60 * 1000L)
                fetchCurrentOrder()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        socket?.disconnect()
        socket?.off()
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}

