package com.uds.foufoufood.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.data_class.model.Speciality
import com.uds.foufoufood.repository.MenuRepository
import kotlinx.coroutines.launch

class MenuViewModel(private val repository: MenuRepository) : ViewModel() {
    private val _menus = MutableLiveData<List<Menu>?>()
    val menus: LiveData<List<Menu>?> get() = _menus

    private val _shared_restaurant = MutableLiveData<Restaurant>()
    val shared_restaurant: LiveData<Restaurant> get() = _shared_restaurant

    private val _shared_current_menu = MutableLiveData<Menu>()
    val shared_current_menu: LiveData<Menu> get() = _shared_current_menu

    private val _isConnectedRestorer = MutableLiveData<Boolean>()
    val isConnectedRestorer: LiveData<Boolean> get() = _isConnectedRestorer

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _categories = MutableLiveData<List<Speciality>?>()
    val categories: LiveData<List<Speciality>?> get() = _categories

    private val _selectedCategory = MutableLiveData<Speciality?>(null)
    val selectedCategory: LiveData<Speciality?> get() = _selectedCategory

    var searchTextCategory by mutableStateOf("")

    private val _filteredMenu = MutableLiveData<List<Menu>?>()
    val filteredMenu: LiveData<List<Menu>?> get() = _filteredMenu

    suspend fun getAllMenusByRestaurant(token: String, restaurantId: String) {
        try {
            val response = repository.getAllMenusByRestaurant(token, restaurantId)

            // Vérifier si la réponse est réussie
            if (response != null && response.success) {
                _menus.postValue(response.data ?: emptyList()) // Mettre à jour le LiveData
                // Retourner la liste des menus ou une liste vide si elle est nulle
            } else {
                Log.e("MenuViewModel", "Erreur d'accès aux menus : ${response?.message}")
            }
        } catch (e: Exception) {
            Log.e("MenuViewModel", "Exception lors de la récupération des menus: ${e.message}", e)
        }
    }

    fun initialize() {
        fetchMenu()
    }

    fun createMenu(
        token: String,
        name: String,
        description: String,
        price: Double,
        restaurantId: String,
        category: String,
        image: String
    ) {
        viewModelScope.launch {
            try {
                val response =
                    repository.createMenu(
                        token,
                        name,
                        description,
                        price,
                        restaurantId,
                        category,
                        image
                    )
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

    fun setIsConnectedRestorer(value:Boolean)
    {
        _isConnectedRestorer.value = value
    }

    fun setSharedCurrentMenu(menu:Menu)
    {
        _shared_current_menu.value = menu
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
        restaurantId: String,
        image: String
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
                    restaurantId,
                    image
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
                                category = category,
                                image = image,
                                description = description
                            )
                            // Remplacer l'ancien menu par le nouveau
                            currentMenus[menuIndex] = updatedMenu
                            if (_shared_current_menu.value?._id == updatedMenu._id)
                                setSharedCurrentMenu(updatedMenu)

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

    private fun getIconCategoryResId(category: String): Int {
        return when (category) {
            "Pizza" -> R.drawable.ic_category_pizza
            "Burger" -> R.drawable.ic_category_burger
            else -> R.drawable.autre
        }
    }

    private fun fetchMenu() {
        viewModelScope.launch {
            val fetchedMenu = repository.getAllMenus()?.data
            if (fetchedMenu != null) {
                _menus.value = fetchedMenu
                _filteredMenu.value = _menus.value
                for ((countCategory, menu) in _menus.value!!.withIndex()) {
                    if (_categories.value?.any { it.name == menu.category } != true) {
                        _categories.value = (_categories.value ?: emptyList()) + Speciality(
                            countCategory,
                            menu.category,
                            getIconCategoryResId(menu.category)
                        )
                    }
                }
            }
        }
    }

    fun onCategorySelected(category: Speciality?) {
        _selectedCategory.value = category
        filterMenus()
    }

    fun onSearchQueryChangedCategory(query: String) {
        searchTextCategory = query
        filterMenus()
    }

    private fun filterMenus() {
        _filteredMenu.value = _menus.value?.filter { menu ->
            // Match by search query
            val matchesQuery = menu.name.contains(searchTextCategory, ignoreCase = true)

            // Match by category (if a category is selected)
            val matchesCategory = _selectedCategory.value?.let {
                menu.category == it.name
            } ?: true // If no category is selected, show all

            matchesQuery && matchesCategory
        }
    }
}

