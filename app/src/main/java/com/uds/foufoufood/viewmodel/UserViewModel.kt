package com.uds.foufoufood.viewmodel

import UserRepository
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.activities.main.TokenManager.deleteToken
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.activities.main.TokenManager.saveToken
import com.uds.foufoufood.activities.main.TokenManager.saveUserId
import com.uds.foufoufood.activities.main.TokenManager.updateToken
import com.uds.foufoufood.data_class.model.Address
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

    private val _loginSuccess = MutableLiveData<Boolean?>()
    val loginSuccess: LiveData<Boolean?> get() = _loginSuccess

    private val _emailValidated = MutableLiveData<Boolean?>()
    val emailValidated: LiveData<Boolean?> get() = _emailValidated

    private val _registrationInitSuccess = MutableLiveData<Boolean?>()
    val registrationInitSuccess: LiveData<Boolean?> get() = _registrationInitSuccess

    private val _codeVerificationSuccess = MutableLiveData<Boolean?>()
    val codeVerificationSuccess: LiveData<Boolean?> get() = _codeVerificationSuccess

    private val _registrationCompleteSuccess = MutableLiveData<Boolean?>()
    val registrationCompleteSuccess: LiveData<Boolean?> get() = _registrationCompleteSuccess

    private val _updateEmailSuccess = MutableLiveData<Boolean?>()
    val updateEmailSuccess: LiveData<Boolean?> get() = _updateEmailSuccess

    private val _updatePasswordSuccess = MutableLiveData<Boolean?>()
    val updatePasswordSuccess: LiveData<Boolean?> get() = _updatePasswordSuccess

    private val _resendCodeEvent = MutableLiveData<Boolean>()

    private val _updateAddressSuccess = MutableLiveData<Boolean?>()
    val updateAddressSuccess: LiveData<Boolean?> get() = _updateAddressSuccess

    // Fonctions
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = userRepository.login(email, password)
                if (response != null) {
                    if (response.user.blockedAccount == false)
                    {
                        _user.value = response.user
                        _token.value = response.token
                        _loginSuccess.value = true

                        if (response.user.emailValidated == false) {
                            _emailValidated.value = false
                            _loading.value = false
                        }
                        else {
                            saveToken(context, response.token)
                            saveUserId(context, response.user._id)
                            _emailValidated.value = true
                            userRepository.setUserEmail(email)
                            Log.d("UserViewModel", "Token JWT sauvegardé : ${response.token}")
                            _errorMessage.value = null
                        }

                        if (response.user.registrationComplete == false) {
                            _registrationCompleteSuccess.value = false
                            _loading.value = false
                        }
                        else {
                            saveToken(context, response.token)
                            saveUserId(context, response.user._id)
                            userRepository.setUserEmail(email)
                            _registrationCompleteSuccess.value = true
                            Log.d("UserViewModel", "Token JWT sauvegardé : ${response.token}")
                            _errorMessage.value = null
                        }
                    }
                    else
                    {
                        _errorMessage.value = "Erreur, connexion échouée car compte bloqué"
                        _loginSuccess.value = false
                    }
                } else {
                    _errorMessage.value = "Erreur, connexion échouée"
                    _loginSuccess.value = false
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
            _loading.value = true
            try {
                val success = userRepository.initiateRegistration(name, email, password)
                _registrationInitSuccess.value = success
            } catch (e: Exception) {
                _registrationInitSuccess.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val success = userRepository.verifyCode(email, code)
                _codeVerificationSuccess.value = success
                _emailValidated.value = success
            } catch (e: Exception) {
                _codeVerificationSuccess.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    fun completeRegistration(email: String, profileType: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = userRepository.completeRegistration(email, profileType)
                if (response != null) {
                    _user.value = response.user
                    _token.value = response.token
                    saveToken(context, response.token)
                    saveUserId(context, response.user._id)
                    userRepository.setUserEmail(response.user.email)
                    _registrationCompleteSuccess.value = true
                } else {
                    _registrationCompleteSuccess.value = false
                }
            } catch (e: Exception) {
                _registrationCompleteSuccess.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    fun resendVerificationCode(email: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val success = userRepository.resendVerificationCode(email)
                _resendCodeEvent.value = success
            } catch (e: Exception) {
                _resendCodeEvent.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    fun getUser(email: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val user = userRepository.getUser(email)
                _user.value = user.body()
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de la récupération de l'utilisateur: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun getUserFromToken(token: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val user = userRepository.getUserFromToken(token)
                _user.value = user
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de la récupération de l'utilisateur: ${e.message}")
            } finally {
                _loading.value = false
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
                deleteToken(context)
            } catch (e: IOException) {
                _errorMessage.value = "Erreur réseau, veuillez vérifier votre connexion"
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de la déconnexion"
            } finally {
                // Réinitialisez les valeurs qui pourraient provoquer des écrans de chargement infinis
                _loading.value = false
                resetStatus() // Réinitialiser tous les états après la déconnexion
            }
        }
    }

    fun updateEmail(previous: String, email: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val token = getToken(context)
                if (token == null) {
                    _errorMessage.value = "Vous n'êtes pas connecté"
                    _loading.value = false
                    _updateEmailSuccess.value = false
                    return@launch
                }
                val response = userRepository.updateEmail(token, previous, email)
                if (response != null) {
                    Log.d("UserViewModel", "Email modifié avec succès")
                    _user.value = response.user
                    Log.d("UserViewModel", "Utilisateur mis à jour : ${response.user}")
                    _token.value = response.token
                    Log.d("UserViewModel", "Token JWT sauvegardé : ${response.token}")
                    updateToken(context, response.token)
                    Log.d("UserViewModel", "Token JWT mis à jour : ${response.token}")
                    userRepository.setUserEmail(email)
                    _updateEmailSuccess.value = true
                    Log.d("UserViewModel", "Success")
                } else {
                    _errorMessage.value = "Erreur lors de la modification de l'email, veuillez réessayer"
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de l'édition de l'email: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun updatePassword(previousPassword: String, newPassword: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val token = getToken(context)
                if (token == null) {
                    _errorMessage.value = "Vous n'êtes pas connecté"
                    _loading.value = false
                    _updatePasswordSuccess.value = false
                    return@launch
                }
                val success = userRepository.updatePassword(token, previousPassword, newPassword)
                if (success) {
                    _errorMessage.value = null
                    _updatePasswordSuccess.value = true
                } else {
                    _errorMessage.value = "Erreur lors de la modification du mot de passe, veuillez réessayer"
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de l'édition du mot de passe: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateAddress(number: Number, street: String, city: String, zipCode: String, state: String, country: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val success = userRepository.updateAddress(number, street, city, zipCode, state, country)
                if (success) {
                    _updateAddressSuccess.value = true
                    _user.value?.address = Address(number, street, city, zipCode, state, country)
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erreur lors de l'édition de l'adresse: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetStatus() {
        _codeVerificationSuccess.value = null
        _registrationInitSuccess.value = null
        _registrationCompleteSuccess.value = null
        _resendCodeEvent.value = false
        _loading.value = false
        _updateAddressSuccess.value = null
        _errorMessage.value = null
        _emailValidated.value = null
        _updateEmailSuccess.value = null
        _updatePasswordSuccess.value = null
        _loginSuccess.value = null
    }
}
