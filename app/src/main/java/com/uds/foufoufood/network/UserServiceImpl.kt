package com.uds.foufoufood.network

import android.util.Log
import com.uds.foufoufood.models.User
import com.uds.foufoufood.request.EmailRequest
import com.uds.foufoufood.request.LoginRequest
import com.uds.foufoufood.request.ProfileRequest
import com.uds.foufoufood.request.RegistrationRequest
import com.uds.foufoufood.request.VerificationRequest
import com.uds.foufoufood.response.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserServiceImpl(private val userApi: UserApi) : UserService {
    override suspend fun login(email: String, password: String): AuthResponse? = withContext(
        Dispatchers.IO){
        Log.d("UserServiceImpl", "login")
        val request = LoginRequest(email, password)
        try {
            Log.d("UserServiceImplTry", "login")
            val response = userApi.login(request).execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun initiateRegistration(name: String, email: String, password: String): Boolean {
        val request = RegistrationRequest(name, email, password)
        val response = userApi.initiateRegistration(request)
        return response.isSuccessful && response.body()?.success == true
    }

    override suspend fun verifyCode(email: String, code: String): Boolean {
        val request = VerificationRequest(email, code)
        val response = userApi.verifyCode(request)
        return response.isSuccessful && response.body()?.success == true
    }

    override suspend fun completeRegistration(email: String, profileType: String): Boolean {
        val request = ProfileRequest(email, profileType)
        val response = userApi.completeRegistration(request)
        return response.isSuccessful && response.body()?.success == true
    }

    override suspend fun resendVerificationCode(email: String) {
        val request = EmailRequest(email)
        userApi.resendVerificationCode(request)
    }
}