package com.uds.foufoufood.network

import com.uds.foufoufood.request.EmailRequest
import com.uds.foufoufood.request.LoginRequest
import com.uds.foufoufood.request.ProfileRequest
import com.uds.foufoufood.request.RegistrationRequest
import com.uds.foufoufood.request.VerificationRequest
import com.uds.foufoufood.response.ApiResponse
import com.uds.foufoufood.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
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
}
