package com.uds.foufoufood.viewmodel

import UserRepository
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.activities.main.TokenManager.saveToken
import com.uds.foufoufood.model.User
import kotlinx.coroutines.launch
import java.io.IOException

class UserViewModel(private val userRepository: UserRepository,
                    private val context: Context
) : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _registrationInitSuccess = MutableLiveData<Boolean>()
    val registrationInitSuccess: LiveData<Boolean> get() = _registrationInitSuccess

    private val _codeVerificationSuccess = MutableLiveData<Boolean>()
    val codeVerificationSuccess: LiveData<Boolean> get() = _codeVerificationSuccess

    private val _registrationCompleteSuccess = MutableLiveData<Boolean>()
    val registrationCompleteSuccess: LiveData<Boolean> get() = _registrationCompleteSuccess

    private val _resendCodeEvent = MutableLiveData<Boolean>()
    val resendCodeEvent: LiveData<Boolean> get() = _resendCodeEvent


    // Fonction pour charger l'utilisateur connecté (par exemple depuis une API ou base de données)
    fun getUserProfile() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val user = userRepository.getUserProfile(_token.value!!)
                _user.value = user?.user
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors du chargement de l'utilisateur"
            } finally {
                _loading.value = false
            }
        }
    }

    // Fonctions
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = userRepository.login(email, password)
                Log.d("UserViewModel", response.toString())
                if (response != null) {
                    _user.value = response.user
                    _token.value = response.token
                    // Stocke le token dans SharedPreferences
                    saveToken(context, response.token)
                    Log.d("UserViewModel", "Token JWT sauvegardé : ${response.token}")
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Connexion échouée"
                }
            } catch (e: IOException) {
                _errorMessage.value = "Erreur réseau, veuillez vérifier votre connexion"
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de la connexion"
            } finally {
                _loading.value = false
            }
        }
    }

    fun initiateRegistration(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val success = userRepository.initiateRegistration(name, email, password)
                _registrationInitSuccess.value = success
            } catch (e: Exception) {
                _registrationInitSuccess.value = false
            }
        }
    }

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            try {
                val success = userRepository.verifyCode(email, code)
                _codeVerificationSuccess.value = success
            } catch (e: Exception) {
                _codeVerificationSuccess.value = false
            }
        }
    }

    fun completeRegistration(email: String, profileType: String) {
        viewModelScope.launch {
            try {
                val success = userRepository.completeRegistration(email, profileType)
                _registrationCompleteSuccess.value = success
            } catch (e: Exception) {
                _registrationCompleteSuccess.value = false
            }
        }
    }


    // Fonction pour charger l'utilisateur connecté (par exemple depuis une API ou base de données)
    fun resendVerificationCode(email: String) {
        viewModelScope.launch {
            try {
                val success = userRepository.resendVerificationCode(email)
                _resendCodeEvent.value = success
            } catch (e: Exception) {
                _resendCodeEvent.value = false
            }
        }
    }
}
