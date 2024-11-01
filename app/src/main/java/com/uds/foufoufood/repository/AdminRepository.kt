package com.uds.foufoufood.repository

import android.content.Context
import android.util.Log
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.data_class.request.RoleUpdateRequest
import com.uds.foufoufood.data_class.response.ApiResponse
import com.uds.foufoufood.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AdminRepository(private val api: UserApi, private val context: Context) {

    suspend fun getAllUsers(): Response<List<User>>? {
        return try {
            val token = "Bearer ${getToken(context)}"
            val response = api.getAllUsers(token)
            if (response.isSuccessful) {
                Log.d(
                    "AdminRepository getAllUsers",
                    "Response from API: ${response.body()?.size} users"
                )
                response
            } else {
                Log.e(
                    "AdminRepository getAllUsers",
                    "Error in getAllUsers, response not successful or null, ${
                        response.errorBody()?.string()
                    }"
                )
                null
            }
        } catch (e: Exception) {
            Log.e("AdminRepository getAllUsers", "Exception in getAllUsers: ${e.message}")
            null
        }
    }

    suspend fun updateUserRole(userEmail: String, newRole: String): ApiResponse {
        Log.d("AdminRepository updateUserRole", "Updating role of user $userEmail to $newRole")
        // Create a RoleUpdateRequest object
        val roleUpdateRequest = RoleUpdateRequest(newRole)

        // Call the API to update the user's role
        val response = api.updateUserRole(userEmail, roleUpdateRequest)

        // Check if the response is successful
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error updating role: response body is null")
        } else {
            throw Exception("Failed to update role: ${response.errorBody()?.string()}")
        }
    }

    suspend fun blockAccount(id: String): ApiResponse? = withContext(Dispatchers.IO) {
        try {
            val response = api.blockAccount(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(
                    "AdminRepository blockAccount",
                    "Problem in blockAccount, response not successful or null"
                )
                null
            }
        } catch (e: Exception) {
            Log.e("AdminRepository blockAccount", e.message ?: "Unknown error")
            null
        }
    }

    suspend fun unlockAccount(id: String): ApiResponse? = withContext(Dispatchers.IO) {
        try {
            val response = api.unlockAccount(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(
                    "AdminRepository unlockAccount",
                    "Problem in unlockAccount, response not successful or null"
                )
                null
            }
        } catch (e: Exception) {
            Log.e("AdminRepository unlockAccount", e.message ?: "Unknown error")
            null
        }
    }

    suspend fun deleteAccount(id: String): ApiResponse? = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteAccount(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(
                    "deleteAccount AdminRepository",
                    "Problem in deleteAccount, response not successful or null"
                )
                null
            }
        } catch (e: Exception) {
            Log.e("deleteAccount AdminRepository", e.message ?: "Unknown error")
            null
        }
    }
}
