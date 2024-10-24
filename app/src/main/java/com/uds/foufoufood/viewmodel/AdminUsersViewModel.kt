package com.uds.foufoufood.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminUsersViewModel(private val repository: AdminRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>?>()
    val users: MutableLiveData<List<User>?> get() = _users

    var selectedRole by mutableStateOf("")
        private set

    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String> get() = _apiError

    var searchText by mutableStateOf("")

    // Filtered list of restaurants based on the selected category and search query
    var filteredUsers by mutableStateOf(listOf<User>())
        private set

    fun fetchUsers(role: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllUsers()
                if (response != null && response.isSuccessful) {
                    val usersList = response.body()
                    Log.d("AdminViewModel", "Réponse API: $usersList")
                    if (usersList != null && usersList.isNotEmpty()) {
                        _users.value = usersList
                        selectedRole = role
                        filterUsers(selectedRole)
                        Log.d(
                            "AdminViewModel",
                            "Nombre d'utilisateurs récupérés: ${usersList.size}"
                        )
                    } else {
                        Log.e("AdminViewModel", "Aucun utilisateur trouvé dans la réponse.")
                    }
                } else {
                    _apiError.value =
                        "Erreur lors de la récupération des utilisateurs: ${response?.message() ?: "Réponse nulle"}"
                    Log.e(
                        "AdminViewModel",
                        "Erreur lors de la récupération des utilisateurs: ${response?.message()}"
                    )
                }
            } catch (e: Exception) {
                _apiError.value = "Erreur: ${e.message}"
                Log.e(
                    "AdminViewModel",
                    "Exception lors de la récupération des utilisateurs: ${e.message}"
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchText = query
        filterUsers(selectedRole)
    }

    fun updateUserRole(user: User, newRole: String) {
        _users.value = _users.value?.map {
            if (it.email == user.email) it.copy(role = newRole) else it
        } ?: emptyList()

        // Synchronisation avec le serveur
        viewModelScope.launch {
            try {
                repository.updateUserRole(user.email, newRole)
            } catch (e: Exception) {
                // Gérer l'erreur de mise à jour
            }
        }
    }

    fun getAll(): List<User> {
        return _users.value ?: emptyList()
    }

    private fun filterUsers(role: String){
        filteredUsers = _users.value?.filter { user ->
            user.role == role && user.email.contains(searchText, ignoreCase = true)
        } ?: emptyList()
    }



}

