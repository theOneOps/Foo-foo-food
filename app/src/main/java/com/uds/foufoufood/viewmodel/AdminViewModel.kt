package com.uds.foufoufood.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        fetchUsers()  // Utilise des données factices pour l'aperçu
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            // Simule des données d'utilisateur
            _users.value = listOf(
                User("1","Alyce Lambo", "alyce.lambo@gmail.com", "https://i.pravatar.cc/150?img=1","Client"),
                User("2","John Doe", "john.doe@example.com", "https://i.pravatar.cc/150?img=2","Restaurateur")
            )
        }
    }
}
