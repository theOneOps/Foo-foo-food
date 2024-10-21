package com.uds.foufoufood.network

import com.uds.foufoufood.model.User
import com.uds.foufoufood.request.EmailRequest
import com.uds.foufoufood.request.LoginRequest
import com.uds.foufoufood.request.ProfileRequest
import com.uds.foufoufood.request.RegistrationRequest
import com.uds.foufoufood.request.VerificationRequest
import com.uds.foufoufood.response.ApiResponse
import com.uds.foufoufood.response.AuthResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @GET("api/users/all")
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): Response<List<User>>

    @POST("api/users/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/users/auth/profile")
    suspend fun getUserProfile(@Body token: String): Response<AuthResponse>

    @POST("api/users/auth/register")
    suspend fun initiateRegistration(@Body registrationRequest: RegistrationRequest): Response<ApiResponse>

    @POST("api/users/auth/verify")
    suspend fun verifyCode(@Body verificationRequest: VerificationRequest): Response<ApiResponse>

    @POST("api/users/auth/complete-profile")
    suspend fun completeRegistration(@Body profileRequest: ProfileRequest): Response<ApiResponse>

    @POST("api/users/auth/resend-code")
    suspend fun resendVerificationCode(@Body emailRequest: EmailRequest): Response<ApiResponse>

    @PUT("api/users/{email}/role")
    suspend fun updateUserRole(
        @Path("email") userEmail: String,       // ID de l'utilisateur
        @Body roleUpdateRequest: RoleUpdateRequest // Corps de la requête avec le nouveau rôle
    ): Response<ApiResponse>
}

data class RoleUpdateRequest(
    val newRole: String  // Nouveau rôle que tu veux attribuer à l'utilisateur
)
