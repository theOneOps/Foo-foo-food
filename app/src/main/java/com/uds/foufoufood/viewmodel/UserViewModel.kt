package com.uds.foufoufood.viewmodel

import UserRepository
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.activities.main.TokenManager.saveToken
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.activities.main.TokenManager.saveUserId
import com.uds.foufoufood.data_class.model.User
import kotlinx.coroutines.launch
import java.io.IOException

class UserViewModel(
    private val userRepository: UserRepository,
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

    private val _codeVerificationSuccess = MutableLiveData<Boolean?>()
    val codeVerificationSuccess: LiveData<Boolean?> get() = _codeVerificationSuccess

    private val _registrationCompleteSuccess = MutableLiveData<Boolean>()
    val registrationCompleteSuccess: LiveData<Boolean> get() = _registrationCompleteSuccess

    private val _resendCodeEvent = MutableLiveData<Boolean>()
    val resendCodeEvent: LiveData<Boolean> get() = _resendCodeEvent

    private val _updateAddressSuccess = MutableLiveData<Boolean>()
    val updateAddressSuccess: LiveData<Boolean> get() = _updateAddressSuccess


    // Fonctions
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = userRepository.login(email, password)
                if (response != null) {
                    _user.value = response.user
                    _token.value = response.token
                    // Stocke le token dans SharedPreferences
                    saveToken(context, response.token)
                    saveUserId(context,response.user._id)
                    userRepository.setUserEmail(email)
                    Log.d("UserViewModel", "Token JWT sauvegardé : ${response.token}")
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Erreur, connexion échouée"
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
                val response = userRepository.completeRegistration(email, profileType)
                _user.value = response?.user
                _token.value = response?.token

                // Stocke le token dans SharedPreferences
                response?.token?.let { saveToken(context, it) }
                response?.user?.email?.let { userRepository.setUserEmail(it) }
                _registrationCompleteSuccess.value = true
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

    fun getUser(email: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUser(email)
                _user.value = user.body()
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de la récupération de l'utilisateur: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _loading.value = true
            try {
                userRepository.logout()
                _user.value = null
                _token.value = null
                _errorMessage.value = null
            } catch (e: IOException) {
                _errorMessage.value = "Erreur réseau, veuillez vérifier votre connexion"
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de la déconnexion"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateEmail(previous: String, email: String) {
        viewModelScope.launch {
            try {
                val token = getToken(context)
                if (token == null) {
                    _errorMessage.value = "Vous n'êtes pas connecté"
                    return@launch
                }
                val success = userRepository.updateEmail(token, previous, email)
                if (success) {
                    _user.value?.email = email
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de l'édition de l'email: ${e.message}")
            }
        }
    }

    fun updatePassword(previousPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val token = getToken(context)
                if (token == null) {
                    _errorMessage.value = "Vous n'êtes pas connecté"
                    return@launch
                }
                val success = userRepository.updatePassword(token, previousPassword, newPassword)
                if (success) {
                    _errorMessage.value = null
                    Toast.makeText(context, "Mot de passe modifié avec succès", Toast.LENGTH_SHORT).show()
                } else {
                    _errorMessage.value = "Erreur lors de la modification du mot de passe, veuillez réessayer"
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de l'édition du mot de passe: ${e.message}")
            }
        }
    }

    fun updateAddress(number: Number, street: String, city: String, zipCode: String, state: String, country: String) {
        Log.d("UserViewModel", "Mise à jour de l'adresse")
        viewModelScope.launch {
            try {
                val success = userRepository.updateAddress(number, street, city, zipCode, state, country)
                if (success) {
                    _updateAddressSuccess.value = true
                    _user.value?.address = Address(number, street, city, zipCode, state, country)
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de l'édition de l'adresse: ${e.message}")
            }
        }
    }

    fun resetStatus() {
        _codeVerificationSuccess.value = null
        _registrationInitSuccess.value = false
        _registrationCompleteSuccess.value = false
        _resendCodeEvent.value = false
        _loading.value = false
        _updateAddressSuccess.value = false
        _errorMessage.value = null
    }
}
