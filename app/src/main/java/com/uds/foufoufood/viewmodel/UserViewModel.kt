package com.uds.foufoufood.viewmodel

import UserRepository
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.models.User
import kotlinx.coroutines.launch
import java.io.IOException

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val user: MutableLiveData<User?> = MutableLiveData()
    val token: MutableLiveData<String?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String?> = MutableLiveData()

    val registrationInitSuccess = MutableLiveData<Boolean>()
    val codeVerificationSuccess = MutableLiveData<Boolean>()
    val registrationCompleteSuccess = MutableLiveData<Boolean>()
    val resendCodeEvent = MutableLiveData<Pair<String, String>>()

    fun login(email: String, password: String) {
        Log.d("UserViewModel", "login - email: $email, password: $password")
        viewModelScope.launch {
            loading.value = true
            try {
                val response = userRepository.login(email, password)
                Log.d("UserViewModel", "login response: $response")
                if (response != null) {
                    user.value = response.user
                    token.value = response.token
                } else {
                    Log.d("UserViewModel", "Connexion échouée")
                    errorMessage.value = "Connexion échouée"
                }
            } catch (e: IOException) {
                // Pour gérer les erreurs réseau
                Log.e("UserViewModel", "Erreur réseau", e)
                errorMessage.value = "Erreur réseau, veuillez vérifier votre connexion"
            } catch (e: Exception) {
                // Pour toute autre exception
                Log.e("UserViewModel", "Erreur lors de la connexion", e)
                errorMessage.value = "Erreur lors de la connexion"
            } finally {
                loading.value = false
                Log.d("UserViewModel", "Login process finished")
            }
        }
    }

    fun initiateRegistration(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val success = userRepository.initiateRegistration(name, email, password)
                registrationInitSuccess.value = success
            } catch (e: Exception) {
                registrationInitSuccess.value = false
            }
        }
    }

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            try {
                val success = userRepository.verifyCode(email, code)
                codeVerificationSuccess.value = success
            } catch (e: Exception) {
                codeVerificationSuccess.value = false
            }
        }
    }

    fun completeRegistration(email: String, profileType: String) {
        viewModelScope.launch {
            try {
                val success = userRepository.completeRegistration(email, profileType)
                registrationCompleteSuccess.value = success
            } catch (e: Exception) {
                registrationCompleteSuccess.value = false
            }
        }
    }

    fun resendVerificationCode(email: String) {
        viewModelScope.launch {
            try {
                userRepository.resendVerificationCode(email)
            } catch (e: Exception) {
                // handle error if needed
            }
        }
    }
}


