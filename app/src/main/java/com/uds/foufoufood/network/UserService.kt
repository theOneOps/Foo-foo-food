package com.uds.foufoufood.network

import com.uds.foufoufood.response.AuthResponse

interface UserService {

    suspend fun login(email: String, password: String): AuthResponse?
    suspend fun initiateRegistration(name: String, email: String, password: String): Boolean
    suspend fun verifyCode(email: String, code: String): Boolean
    suspend fun completeRegistration(email: String, profileType: String): Boolean
    suspend fun resendVerificationCode(email: String)
}