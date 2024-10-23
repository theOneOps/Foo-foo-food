package com.uds.foufoufood.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.Speciality
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.repository.RestaurantRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val restaurantRepository: RestaurantRepository) : ViewModel() {

    // List of categories and restaurants
    var specialities by mutableStateOf(listOf<Speciality>())
        private set

    var restaurants by mutableStateOf(listOf<Restaurant>())
        private set

    // For tracking selected category and search query
    var selectedSpeciality by mutableStateOf<Speciality?>(null)
        private set

    var searchText by mutableStateOf("")

    // Filtered list of restaurants based on the selected category and search query
    var filteredRestaurants by mutableStateOf(listOf<Restaurant>())
        private set

    // Initialize categories and restaurants (you can load from a repository or hardcode)
    fun initialize(specialityList: List<Speciality>,) {
        specialities = specialityList
        fetchRestaurants()
    }

    // Fetch restaurants from the backend
    private fun fetchRestaurants() {
        viewModelScope.launch {
            val fetchedRestaurants = restaurantRepository.getAllRestaurants()
            if (fetchedRestaurants != null) {
                restaurants = fetchedRestaurants
                filteredRestaurants = restaurants
            }
        }
    }

    // Handle category selection
    fun onSpecialitySelected(speciality: Speciality?) {
        selectedSpeciality = speciality
        filterRestaurants()
    }

    // Handle search query changes
    fun onSearchQueryChanged(query: String) {
        searchText = query
        filterRestaurants()
    }

    // Filter restaurants based on the selected category and search query
    private fun filterRestaurants() {
        filteredRestaurants = restaurants.filter { restaurant ->
            // Match by search query
            val matchesQuery = restaurant.name.contains(searchText, ignoreCase = true)

            // Match by category (if a category is selected)
            val matchesSpeciality = selectedSpeciality?.let {
                restaurant.speciality == it.name
            } ?: true // If no category is selected, show all

            matchesQuery && matchesSpeciality
        }
    }
}
