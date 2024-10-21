package com.uds.foufoufood.repository

import android.content.Context
import android.util.Log
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.data_class.response.ApiResponse
import com.uds.foufoufood.network.RoleUpdateRequest
import com.uds.foufoufood.network.UserApi
import retrofit2.Response

class AdminRepository(private val api: UserApi, private val context: Context) {

    suspend fun getAllUsers(): Response<List<User>>? {
        return try {
            val token = "Bearer ${getToken(context)}"
            val response = api.getAllUsers(token)
            if (response.isSuccessful) {
                Log.d(
                    "AdminRepository",
                    "Réponse API réussie, utilisateurs récupérés: ${response.body()?.size}"
                )
                response
            } else {
                Log.e(
                    "AdminRepository",
                    "Erreur lors de l'appel API: ${response.errorBody()?.string()}"
                )
                null
            }
        } catch (e: Exception) {
            Log.e("AdminRepository", "Exception lors de l'appel API: ${e.message}")
            null
        }
    }

    suspend fun updateUserRole(userEmail: String, newRole: String): ApiResponse {
        // Crée la requête pour mettre à jour le rôle
        val roleUpdateRequest = RoleUpdateRequest(newRole)

        // Appel à l'API via Retrofit pour mettre à jour le rôle
        val response = api.updateUserRole(userEmail, roleUpdateRequest)

        // Vérification de la réponse de l'API
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Erreur lors de la mise à jour du rôle")
        } else {
            throw Exception("Échec de la mise à jour du rôle : ${response.errorBody()?.string()}")
        }
    }
}
