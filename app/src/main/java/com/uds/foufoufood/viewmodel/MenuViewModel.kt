package com.uds.foufoufood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.repository.MenuRepository
import kotlinx.coroutines.launch

class MenuViewModel(private val repository: MenuRepository) : ViewModel() {
    private val _menus = MutableLiveData<List<Menu>?>()
    val menus: LiveData<List<Menu>?> get() = _menus

    private val _shared_restaurant = MutableLiveData<Restaurant>()
    val shared_restaurant: LiveData<Restaurant> get() = _shared_restaurant

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    suspend fun getAllMenus(token: String, restaurantId: String): List<Menu> {
        return try {
            val response = repository.getAllMenus(token, restaurantId)

            // Vérifier si la réponse est réussie
            if (response != null && response.success) {
                return response.data
                    ?: emptyList() // Retourner la liste des menus ou une liste vide si elle est nulle
            } else {
                Log.e("MenuViewModel", "Erreur d'accès aux menus : ${response?.message}")
                emptyList() // Retourner une liste vide si la réponse n'est pas un succès
            }
        } catch (e: Exception) {
            Log.e("MenuViewModel", "Exception lors de la récupération des menus: ${e.message}", e)
            emptyList() // Retourner une liste vide en cas d'exception
        }
    }


    fun createMenu(
        token: String,
        name: String,
        description: String,
        price: Double,
        restaurantId: String,
        category: String
    ) {
        viewModelScope.launch {
            try {
                val response =
                    repository.createMenu(token, name, description, price, restaurantId, category)
                if (response != null) {
                    if (response.success) {
                        val currentMenus = _menus.value?.toMutableList() ?: mutableListOf()
                        response.data?.let { currentMenus.add(it[0]) }
                        _menus.value = currentMenus
                    } else {
                        _errorMessage.value = response.message
                    }
                } else {
                    _errorMessage.value =
                        "Erreur lors de la creation de menu: ${response?.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
                Log.e(
                    "MenuViewModel",
                    "Exception lors de la creation de menu : ${e.message}",
                    e
                )
            }
        }
    }

    fun setSharedRestaurant(restaurant: Restaurant) {
        _shared_restaurant.value = restaurant
    }

    fun deleteMenu(token: String, menuId: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteMenu(token, menuId)
                if (response != null) {
                    if (response.success) {
                        val currentMenus = _menus.value?.toMutableList() ?: mutableListOf()
//                        val newMenusList = currentMenus.filter { it.menuId != menuId }
//                        _menus.value = newMenusList
                        currentMenus.removeIf { it._id == menuId }
                        _menus.value = currentMenus
                    } else {
                        _errorMessage.value = response.message // Afficher le message d'erreur
                    }
                } else {
                    _errorMessage.value =
                        "Erreur lors de la suppression du menu: ${response?.message}"

                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}" // Loguer l'exception
                Log.e(
                    "MenuViewModel",
                    "Exception lors de la suppresion du menu: ${e.message}",
                    e
                )
            }
        }
    }

    fun updateMenu(
        token: String,
        menuId: String,
        name: String,
        description: String,
        price: Double,
        category: String,
        restaurantId: String
    ) {
        viewModelScope.launch {
            try {
                val response = repository.updateMenu(
                    token,
                    menuId,
                    name,
                    description,
                    price,
                    category,
                    restaurantId
                )
                if (response != null) {
                    if (response.success) {
                        val currentMenus = _menus.value?.toMutableList() ?: mutableListOf()

                        // Chercher le menu correspondant à menuId
                        val menuIndex = currentMenus.indexOfFirst { it._id == menuId }

                        // Si le menu est trouvé, le mettre à jour
                        if (menuIndex != -1) {
                            val updatedMenu = currentMenus[menuIndex].copy(
                                name = name,
                                price = price,
                                category = category
                            )
                            // Remplacer l'ancien menu par le nouveau
                            currentMenus[menuIndex] = updatedMenu

                            // Mettre à jour la liste des menus
                            _menus.value = currentMenus
                        } else {
                            _errorMessage.value = "Menu non trouvé pour modification"
                        }
                    } else {
                        _errorMessage.value =
                            response.message // Afficher le message d'erreur de l'API
                    }
                } else {
                    _errorMessage.value = "Erreur lors de la modification du menu"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}" // Loguer l'exception
                Log.e(
                    "MenuViewModel",
                    "Exception lors de la modification du menu: ${e.message}",
                    e
                )
            }
        }
    }
}

