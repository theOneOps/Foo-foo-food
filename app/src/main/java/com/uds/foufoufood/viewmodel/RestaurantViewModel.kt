package com.uds.foufoufood.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.main.TokenManager.getUserId
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.data_class.model.Speciality
import com.uds.foufoufood.repository.RestaurantRepository
import kotlinx.coroutines.launch

class RestaurantViewModel(private val restaurantRepository: RestaurantRepository) : ViewModel(){
    private val _specialities = MutableLiveData<List<Speciality>?>()
    val specialities: LiveData<List<Speciality>?> get() = _specialities

    private val _restaurants = MutableLiveData<List<Restaurant>?>()
    val restaurants: LiveData<List<Restaurant>?> get() = _restaurants

    private val _selectedSpeciality = MutableLiveData<Speciality?>()
    val selectedSpeciality: LiveData<Speciality?> get() = _selectedSpeciality

    var searchText by mutableStateOf("")

    private val _filteredRestaurants = MutableLiveData<List<Restaurant>?>()
    val filteredRestaurants: LiveData<List<Restaurant>?> get() = _filteredRestaurants

    private val _idRestaurantOfConnectedUser = MutableLiveData<String>()
    val idRestaurantOfConnectedUser: LiveData<String> get() = _idRestaurantOfConnectedUser

    fun initialize(context: Context) {
        val userIdConnected = getUserId(context)
        if (userIdConnected != null) {
            fetchRestaurants(userIdConnected)
        }
    }

    private fun getIconSpecialityResId(speciality: String): Int {
        Log.d("RestaurantViewModel", "Speciality: $speciality")
        return when (speciality) {
            "USA" -> R.drawable.usa
            "Français" -> R.drawable.france
            "Japonais" -> R.drawable.japon
            "Italien" -> R.drawable.italie
            else -> R.drawable.autre
        }
    }

    // Fetch restaurants from the backend
    private fun fetchRestaurants(userIdConnected: String) {
        viewModelScope.launch {
            val fetchedRestaurants = restaurantRepository.getAllRestaurants()
            if (fetchedRestaurants != null) {
                _restaurants.value = fetchedRestaurants
                _filteredRestaurants.value = _restaurants.value
                for ((countCategory, restaurant) in _restaurants.value!!.withIndex()) {
                    if (restaurant.userId == userIdConnected) {
                        _idRestaurantOfConnectedUser.value = restaurant._id
                    }
                    if (_specialities.value?.any { it.name == restaurant.speciality } != true) {
                        _specialities.value = (_specialities.value ?: emptyList()) + Speciality(
                            countCategory,
                            restaurant.speciality,
                            getIconSpecialityResId(restaurant.speciality)
                        )
                    }
                }
            }
        }
    }

    // Handle category selection
    fun onSpecialitySelected(speciality: Speciality?) {
        _selectedSpeciality.value = speciality
        filterRestaurants()
    }

    // Handle search query changes
    fun onSearchQueryChangedSpeciality(query: String) {
        searchText = query
        filterRestaurants()
    }

    // Filter restaurants based on the selected category and search query
    private fun filterRestaurants() {
        _filteredRestaurants.value = _restaurants.value?.filter { restaurant ->
            // Match by search query
            val matchesQuery = restaurant.name.contains(searchText, ignoreCase = true)

            // Match by category (if a category is selected)
            val matchesSpeciality = _selectedSpeciality.value?.let {
                restaurant.speciality == it.name
            } ?: true // If no category is selected, show all

            matchesQuery && matchesSpeciality
        }
    }

    fun updateRestaurant(id: String, restaurant: Restaurant) {
        viewModelScope.launch {
            try {
                val response = restaurantRepository.updateRestaurant( id, restaurant)
                if (response != null) {
                    if (response.success)
                    {
                        val currentRestaurants = restaurants.value?.toMutableList() ?: mutableListOf()

                        val restaurantIndex = currentRestaurants.indexOfFirst { it._id == id }

                        if (restaurantIndex != -1) {
                            currentRestaurants[restaurantIndex] = restaurant
                        }
                        _restaurants.value = currentRestaurants
                    }
                }
            } catch (e: Exception) {
                Log.d(
                    "HomeViewModel",
                    "Exception lors de la modification du restaurant: ${e.message}"
                )
            }
        }
    }
}