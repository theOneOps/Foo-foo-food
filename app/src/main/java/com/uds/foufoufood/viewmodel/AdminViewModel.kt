package com.uds.foufoufood.viewmodel

import UserRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.User
import kotlinx.coroutines.launch
import com.uds.foufoufood.repository.AdminRepository

class AdminViewModel(private val repository: AdminRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>?>()
    val users: MutableLiveData<List<User>?> get() = _users

    private val _apiError = MutableLiveData<String>()
    val apiError: LiveData<String> get() = _apiError

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = repository.getAllUsers()
                if (response != null && response.isSuccessful) {
                    val usersList = response.body()
                    Log.d("AdminViewModel", "Réponse API: $usersList")
                    if (usersList != null && usersList.isNotEmpty()) {
                        _users.value = usersList
                        Log.d("AdminViewModel", "Nombre d'utilisateurs récupérés: ${usersList.size}")
                    } else {
                        Log.e("AdminViewModel", "Aucun utilisateur trouvé dans la réponse.")
                    }
                } else {
                    _apiError.value = "Erreur lors de la récupération des utilisateurs: ${response?.message() ?: "Réponse nulle"}"
                    Log.e("AdminViewModel", "Erreur lors de la récupération des utilisateurs: ${response?.message()}")
                }
            } catch (e: Exception) {
                _apiError.value = "Erreur: ${e.message}"
                Log.e("AdminViewModel", "Exception lors de la récupération des utilisateurs: ${e.message}")
            }
        }
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

    // Filtrer les utilisateurs par rôle
    fun getClients(): List<User> {
        val usersList = _users.value
        if (usersList.isNullOrEmpty()) {
            Log.e("AdminViewModel", "Aucun utilisateur chargé")
            return emptyList()  // Retourne une liste vide si aucun utilisateur n'est chargé
        }
        // Log pour vérifier le filtre
        val clients = usersList.filter { it.role == "client" }
        Log.d("AdminViewModel", "Nombre de clients: ${clients.size}")
        return clients
    }


    fun getLivreurs(): List<User> {
        return _users.value?.filter { it.role == "livreur" } ?: emptyList()
    }

    fun getGerants(): List<User> {
        return _users.value?.filter { it.role == "restaurateur" } ?: emptyList()
    }

    fun getAll(): List<User> {
        return _users.value ?: emptyList()
    }
}

