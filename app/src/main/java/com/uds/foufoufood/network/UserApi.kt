package com.uds.foufoufood.network

import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.data_class.request.AddressRequest
import com.uds.foufoufood.data_class.request.AvailabilityRequest
import com.uds.foufoufood.data_class.request.EmailRequest
import com.uds.foufoufood.data_class.request.LoginRequest
import com.uds.foufoufood.data_class.request.PasswordRequest
import com.uds.foufoufood.data_class.request.ProfileRequest
import com.uds.foufoufood.data_class.request.RegisterFcmTokenRequest
import com.uds.foufoufood.data_class.request.RegistrationRequest
import com.uds.foufoufood.data_class.request.RoleUpdateRequest
import com.uds.foufoufood.data_class.request.UpdateEmailRequest
import com.uds.foufoufood.data_class.request.VerificationRequest
import com.uds.foufoufood.data_class.response.ApiResponse
import com.uds.foufoufood.data_class.response.AuthResponse
import com.uds.foufoufood.data_class.response.AvailabilityResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @GET("api/users/user/{email}")
    suspend fun getUser(@Path("email") userEmail: String): Response<User>

    @GET("api/users/user")
    suspend fun getUserFromToken(@Header("Authorization") token: String): Response<User>

    @POST("api/users/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/users/auth/register")
    suspend fun initiateRegistration(@Body registrationRequest: RegistrationRequest): Response<ApiResponse>

    @POST("api/users/auth/verify")
    suspend fun verifyCode(@Body verificationRequest: VerificationRequest): Response<ApiResponse>

    @POST("api/users/auth/complete-profile")
    suspend fun completeRegistration(@Body profileRequest: ProfileRequest): Response<AuthResponse>

    @POST("api/users/auth/resend-code")
    suspend fun resendVerificationCode(@Body emailRequest: EmailRequest): Response<ApiResponse>

    @POST("api/users/auth/logout")
    suspend fun logout(): Response<ApiResponse>

    @PUT("api/users/auth/update-email")
    suspend fun updateEmail(@Header("Authorization") token: String,
                            @Body emailRequest: UpdateEmailRequest
    ): Response<AuthResponse>

    @PUT("api/users/auth/update-password")
    suspend fun updatePassword(@Header("Authorization") token: String,
                               @Body passwordRequest: PasswordRequest
    ): Response<ApiResponse>

    @PUT("api/users/auth/update-address")
    suspend fun updateAddress(@Body addressRequest: AddressRequest): Response<ApiResponse>

    @PUT("api/users/{email}/role")
    suspend fun updateUserRole(
        @Path("email") userEmail: String,       // ID de l'utilisateur
        @Body roleUpdateRequest: RoleUpdateRequest // Corps de la requête avec le nouveau rôle
    ): Response<ApiResponse>

    @PUT("api/users/delivery/deliveryAvailability")
    suspend fun updateUserAvailability(
        @Header("Authorization") token: String,
        @Body deliveryAvailability : AvailabilityRequest
    ): Response<ApiResponse>

    @GET("api/users/delivery/deliveryAvailability")
    suspend fun getAvailability(
        @Header("Authorization") token: String
    ): Response<AvailabilityResponse>

    @PUT("api/users/register-fcm-token")
    suspend fun registerFcmToken(
        @Header("Authorization") token: String,
        @Body request: RegisterFcmTokenRequest
    ): Response<Void>

    @PUT("api/users/block-account/{id}")
    suspend fun blockAccount(@Path("id") id:String):Response<ApiResponse>

    @PUT("api/users/unlock-account/{id}")
    suspend fun unlockAccount(@Path("id") id:String):Response<ApiResponse>

    @DELETE("api/users/delete-account/{id}")
    suspend fun deleteAccount(@Path("id") id:String):Response<ApiResponse>
}



