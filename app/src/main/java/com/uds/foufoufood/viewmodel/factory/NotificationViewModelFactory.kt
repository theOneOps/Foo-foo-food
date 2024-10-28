package com.uds.foufoufood.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uds.foufoufood.viewmodel.NotificationViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

class NotificationViewModelFactory(
    private val userViewModel: UserViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(userViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}