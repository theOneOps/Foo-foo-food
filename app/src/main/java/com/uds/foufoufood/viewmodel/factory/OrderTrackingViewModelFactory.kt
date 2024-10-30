// OrderTrackingViewModelFactory.kt
package com.uds.foufoufood.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uds.foufoufood.repository.OrderRepository
import com.uds.foufoufood.viewmodel.OrderTrackingViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

class OrderTrackingViewModelFactory(
    private val orderRepository: OrderRepository, private val userViewModel: UserViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderTrackingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return OrderTrackingViewModel(
                orderRepository,
                userViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
