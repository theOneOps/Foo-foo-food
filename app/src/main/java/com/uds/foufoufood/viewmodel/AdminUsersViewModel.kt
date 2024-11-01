package com.uds.foufoufood.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminUsersViewModel(private val repository: AdminRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>?>()
    val users: MutableLiveData<List<User>?> get() = _users

    private var selectedRole by mutableStateOf("")

    private val _apiError = MutableLiveData<String>()

    var searchText by mutableStateOf("")

    private var _filteredUsers = MutableLiveData<List<User>?>()
    val filteredUsers: MutableLiveData<List<User>?> get() = _filteredUsers

    fun fetchUsers(role: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllUsers()
                if (response != null && response.isSuccessful) {
                    val usersList = response.body()
                    if (!usersList.isNullOrEmpty()) {
                        _users.value = usersList
                        selectedRole = role
                        filterUsers(selectedRole)
                    } else {
                        Log.e("AdminViewModel fetchUsers", "Users list is empty")
                    }
                } else {
                    _apiError.value =
                        "Error fetching users: ${response?.message() ?: "Unknown error"}"
                    Log.e(
                        "AdminViewModel fetchUsers", "Error fetching users: ${response?.message()}"
                    )
                }
            } catch (e: Exception) {
                _apiError.value = "Error : ${e.message}"
                Log.e(
                    "AdminViewModel fetchUsers",
                    "Failed to fetch users: ${e.message}"
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

        viewModelScope.launch {
            try {
                repository.updateUserRole(user.email, newRole)
            } catch (e: Exception) {
            }
        }
    }

    fun getAll(): List<User> {
        return _users.value ?: emptyList()
    }

    private fun filterUsers(role: String) {
        _filteredUsers.value = _users.value?.filter { user ->
            user.role == role && user.email.contains(searchText, ignoreCase = true)
        } ?: emptyList()
    }

    fun blockAccount(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.blockAccount(id)
                if (response != null) {
                    if (response.success)
                        Log.d(
                        "AdminUsersViewModel blockAccount",
                        "Account blocked successfully"
                    )
                } else Log.e(
                    "AdminUsersViewModel blockAccount", "Response from blockAccount null"
                )
            } catch (e: Exception) {
                e.message?.let { Log.e("AdminUsersViewModel blockAccount", it) }
            }
        }
    }

    fun unlockAccount(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.unlockAccount(id)
                if (response != null) {
                    if (response.success)
                        Log.d(
                        "AdminUsersViewModel unlockAccount",
                        "Account unlocked successfully"
                    )
                } else Log.e(
                    "AdminUsersViewModel unlockAccount", "Response from unlockAccount null"
                )
            } catch (e: Exception) {
                e.message?.let { Log.e("AdminUsersViewModel unlockAccount", it)}
            }
        }
    }

    fun deleteAccount(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteAccount(id)
                if (response != null) {
                    if (response.success) Log.d(
                        "AdminUsersViewModel deleteAccount", "Account deleted successfully"
                    )
                } else Log.e(
                    "AdminUsersViewModel deleteAccount", "Response from deleteAccount null"
                )
            } catch (e: Exception) {
                e.message?.let { Log.e("AdminUsersViewModel deleteAccount", it) }
            }
        }
    }
}