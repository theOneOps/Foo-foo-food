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
    private val menus: LiveData<List<Menu>?> get() = _menus

    private val _sortOrder = MutableLiveData<Int?>()
    val sortOrder: LiveData<Int?> get() = _sortOrder

    private val _sortedMenus = MutableLiveData<List<Menu>>()
    val sortedMenus: LiveData<List<Menu>> get() = _sortedMenus

    private val _sharedRestaurant = MutableLiveData<Restaurant>()
    val sharedRestaurant: LiveData<Restaurant> get() = _sharedRestaurant

    private val _sharedCurrentMenu = MutableLiveData<Menu>()
    val sharedCurrentMenu: LiveData<Menu> get() = _sharedCurrentMenu

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

    fun updateSortOrder(sortOrder: Int) {
        _sortOrder.value = sortOrder
        applySort()
    }

    private fun applySort() {
        _menus.value?.let { menus ->
            _sortedMenus.value = when (_sortOrder.value) {
                0 -> menus.sortedBy { it.price }
                1 -> menus.sortedByDescending { it.price }
                else -> menus
            }
        }
    }

    suspend fun getAllMenusByRestaurant(token: String, restaurantId: String) {
        try {
            val response = repository.getAllMenusByRestaurant(token, restaurantId)
            if (response != null && response.success) {
                _menus.postValue(response.data ?: emptyList())
                _sortedMenus.postValue(response.data ?: emptyList())
            } else {
                Log.e(
                    "MenuViewModel getAllMenusByRestaurant",
                    "Error fetching menus: ${response?.message}"
                )
            }
        } catch (e: Exception) {
            Log.e("MenuViewModel getAllMenusByRestaurant", "Failed to fetch menus: ${e.message}")
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
        image: String,
        ingredients: List<String>
    ) {
        viewModelScope.launch {
            try {
                val response = repository.createMenu(
                    token, name, description, price, restaurantId, category, image, ingredients
                )
                if (response != null) {
                    if (response.success) {
                        val currentMenus = _menus.value?.toMutableList() ?: mutableListOf()
                        response.data?.let { currentMenus.add(it[0]) }
                        _menus.value = currentMenus
                        _sortedMenus.value = currentMenus
                    } else {
                        _errorMessage.value = response.message
                    }
                } else {
                    _errorMessage.value = "Erreur lors de la creation de menu"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
                Log.e(
                    "MenuViewModel createMenu", "Exception creating menu: ${e.message}", e
                )
            }
        }
    }

    fun setSharedRestaurant(restaurant: Restaurant) {
        _sharedRestaurant.value = restaurant
    }

    fun setIsConnectedRestorer(value: Boolean) {
        _isConnectedRestorer.value = value
    }

    fun setSharedCurrentMenu(menu: Menu) {
        _sharedCurrentMenu.value = menu
    }

    fun deleteMenu(token: String, menuId: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteMenu(token, menuId)
                if (response != null) {
                    if (response.success) {
                        val currentMenus = _menus.value?.toMutableList() ?: mutableListOf()
                        currentMenus.removeIf { it._id == menuId }
                        _menus.value = currentMenus
                        _sortedMenus.value = currentMenus
                    } else {
                        _errorMessage.value = response.message
                    }
                } else {
                    _errorMessage.value = "Erreur lors de la suppression du menu"

                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}"
                Log.e(
                    "MenuViewModel deleteMenu", "Exception deleting menu: ${e.message}", e
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
        image: String,
        ingredients: List<String>
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
                    image,
                    ingredients
                )
                if (response != null) {
                    if (response.success) {
                        val currentMenus = _menus.value?.toMutableList() ?: mutableListOf()

                        val menuIndex = currentMenus.indexOfFirst { it._id == menuId }

                        if (menuIndex != -1) {
                            val updatedMenu = currentMenus[menuIndex].copy(
                                name = name,
                                price = price,
                                category = category,
                                image = image,
                                description = description
                            )

                            currentMenus[menuIndex] = updatedMenu
                            if (_sharedCurrentMenu.value?._id == updatedMenu._id) setSharedCurrentMenu(
                                updatedMenu
                            )

                            _menus.value = currentMenus
                            _sortedMenus.value = currentMenus
                        } else {
                            _errorMessage.value = "Menu non trouvé pour modification"
                        }
                    } else {
                        _errorMessage.value = response.message
                    }
                } else {
                    _errorMessage.value = "Erreur lors de la modification du menu"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.message}" // Loguer l'exception
                Log.e(
                    "MenuViewModel updateMenu", "Exception updating menu: ${e.message}", e
                )
            }
        }
    }

    private fun getIconCategoryResId(category: String): Int {
        return when (category) {
            "pizza" -> R.drawable.pizza
            "nouilles" -> R.drawable.nouilles
            "burger" -> R.drawable.burger
            "hotdog" -> R.drawable.hotdog
            "frite" -> R.drawable.frites
            "dessert" -> R.drawable.dessert
            "quiche" -> R.drawable.quiche
            "sandwich" -> R.drawable.sandwich
            "sushi" -> R.drawable.sushi
            "maki" -> R.drawable.maki
            "pâte" -> R.drawable.pates
            "gnocchi" -> R.drawable.gnocchi
            "salade" -> R.drawable.salade
            else -> R.drawable.autre
        }
    }

    private fun fetchMenu() {
        viewModelScope.launch {
            val fetchedMenu = repository.getAllMenus()?.data
            if (fetchedMenu != null) {
                _menus.value = fetchedMenu
                applySort()
                _filteredMenu.value = _menus.value
                for ((countCategory, menu) in _menus.value!!.withIndex()) {
                    if (_categories.value?.any { it.name == menu.category } != true) {
                        _categories.value = (_categories.value ?: emptyList()) + Speciality(
                            countCategory, menu.category, getIconCategoryResId(menu.category)
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
            val matchesQuery = menu.name.contains(searchTextCategory, ignoreCase = true)

            val matchesCategory = _selectedCategory.value?.let {
                menu.category == it.name
            } ?: true
            matchesQuery && matchesCategory
        }
    }
}

