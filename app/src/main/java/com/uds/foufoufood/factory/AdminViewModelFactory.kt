package com.uds.foufoufood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uds.foufoufood.repository.AdminRepository
import com.uds.foufoufood.viewmodel.AdminViewModel

class AdminViewModelFactory(private val repository: AdminRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            return AdminViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}