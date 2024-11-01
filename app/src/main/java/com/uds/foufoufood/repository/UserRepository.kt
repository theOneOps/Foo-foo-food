package com.uds.foufoufood.repository

import android.content.Context
import android.util.Log
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.data_class.request.AddressRequest
import com.uds.foufoufood.data_class.request.EmailRequest
import com.uds.foufoufood.data_class.request.LoginRequest
import com.uds.foufoufood.data_class.request.PasswordRequest
import com.uds.foufoufood.data_class.request.ProfileRequest
import com.uds.foufoufood.data_class.request.RegisterFcmTokenRequest
import com.uds.foufoufood.data_class.request.RegistrationRequest
import com.uds.foufoufood.data_class.request.TokenGoogleRequest
import com.uds.foufoufood.data_class.request.UpdateEmailRequest
import com.uds.foufoufood.data_class.request.VerificationRequest
import com.uds.foufoufood.data_class.response.AuthResponse
import com.uds.foufoufood.data_class.response.LoginGoogleResponse
import com.uds.foufoufood.data_class.response.RegisterGoogleResponse
import com.uds.foufoufood.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository(private val userApi: UserApi, private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    suspend fun getUser(email: String): Response<User> = withContext(Dispatchers.IO) {
        return@withContext userApi.getUser(email)
    }

    suspend fun getUserFromToken(token: String): User? = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = userApi.getUserFromToken("Bearer $token")
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(
                    "UserRepository getUserFromToken",
                    "Error getting user from token: ${response.code()}"
                )
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository getUserFromToken", "Network error: ${e.message}")
            null
        }
    }

    suspend fun login(email: String, password: String): AuthResponse? =
        withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email, password)
                val response = userApi.login(request)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("UserRepository login", "Error logging in: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("UserRepository login", "Network error: ${e.message}")
                null
            }
        }

    suspend fun initiateRegistration(name: String, email: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val request = RegistrationRequest(name, email, password)
                val response = userApi.initiateRegistration(request)
                response.isSuccessful && response.body()?.success == true
            } catch (e: Exception) {
                Log.e(
                    "UserRepository initiateRegistration",
                    "Error initiating registration: ${e.message}"
                )
                false
            }
        }

    suspend fun verifyCode(email: String, code: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = VerificationRequest(email, code)
            val response = userApi.verifyCode(request)
            response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            Log.e("UserRepository verifyCode", "Error verifying code: ${e.message}")
            false
        }
    }

    suspend fun completeRegistration(email: String, profileType: String): AuthResponse? =
        withContext(Dispatchers.IO) {
            try {
                val request = ProfileRequest(email, profileType)
                val response = userApi.completeRegistration(request)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e(
                        "UserRepository completeRegistration",
                        "Error completing registration: ${response.code()}"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.e("UserRepository completeRegistration", "Network error: ${e.message}")
                null
            }
        }

    suspend fun resendVerificationCode(email: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = EmailRequest(email)
            val response = userApi.resendVerificationCode(request)
            response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            Log.e(
                "UserRepository resendVerificationCode",
                "Error resending verification code: ${e.message}"
            )
            false
        }
    }

    suspend fun logout(): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = userApi.logout()
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("UserRepository logout", "Error logging out: ${e.message}")
            false
        }
    }

    suspend fun updateEmail(token: String, previous: String, email: String): AuthResponse? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response =
                    userApi.updateEmail("Bearer $token", UpdateEmailRequest(previous, email))
                response.body()
            } catch (e: Exception) {
                Log.e("UserRepository updateEmail", "Error updating email: ${e.message}")
                null
            }
        }

    suspend fun updatePassword(
        token: String, previousPassword: String, newPassword: String
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = userApi.updatePassword(
                "Bearer $token", PasswordRequest(previousPassword, newPassword)
            )
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("UserRepository updatePassword", "Error updating password: ${e.message}")
            false
        }
    }

    suspend fun updateAddress(
        number: Number,
        street: String,
        city: String,
        state: String,
        zipCode: String,
        country: String
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response =
                userApi.updateAddress(AddressRequest(number, street, city, state, zipCode, country))
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("UserRepository updateAddress", "Error updating address: ${e.message}")
            false
        }
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("user_email", null)
    }

    fun setUserEmail(email: String) {
        sharedPreferences.edit().putString("user_email", email).apply()
    }

    suspend fun registerFcmToken(email: String, fcmToken: String): Boolean {
        return try {
            val request = RegisterFcmTokenRequest(email, fcmToken)
            val response = userApi.registerFcmToken("Bearer ${getToken(context)}", request)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("UserRepository registerFcmToken", "Error registering FCM token: ${e.message}")
            false
        }
    }

    suspend fun loginWithGoogle(idToken: String): LoginGoogleResponse? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = userApi.loginWithGoogle(TokenGoogleRequest(idToken))
                response.body()
            } catch (e: Exception) {
                Log.e(
                    "UserRepository loginWithGoogle", "Error logging in with Google: ${e.message}"
                )
                null
            }
        }

    suspend fun registerWithGoogle(idToken: String): RegisterGoogleResponse? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = userApi.registerWithGoogle(TokenGoogleRequest(idToken))
                response.body()
            } catch (e: Exception) {
                Log.e(
                    "UserRepository registerWithGoogle",
                    "Error registering with Google: ${e.message}"
                )
                null
            }
        }
}
