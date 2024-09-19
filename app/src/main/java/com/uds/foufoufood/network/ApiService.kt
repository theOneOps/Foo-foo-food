package com.uds.foufoufood.network

import com.uds.foufoufood.models.AuthResponse
import com.uds.foufoufood.models.LoginRequest
import com.uds.foufoufood.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // Route pour l'inscription
    @POST("/api/users/register")
    fun registerUser(@Body user: User): Call<AuthResponse>

    // Route pour la connexion
    @POST("/api/users/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<AuthResponse>
}