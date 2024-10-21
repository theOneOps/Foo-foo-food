package com.uds.foufoufood.viewmodel

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.uds.foufoufood.model.Category
import com.uds.foufoufood.model.Restaurant

class HomeViewModel : ViewModel() {

    // List of categories and restaurants
    var categories by mutableStateOf(listOf<Category>())
        private set

    var restaurants by mutableStateOf(listOf<Restaurant>())
        private set

    // For tracking selected category and search query
    var selectedCategory by mutableStateOf<Category?>(null)
        private set

    var searchText by mutableStateOf("")

    // Filtered list of restaurants based on the selected category and search query
    var filteredRestaurants by mutableStateOf(listOf<Restaurant>())
        private set

    // Initialize categories and restaurants (you can load from a repository or hardcode)
    fun initialize(categoriesList: List<Category>, restaurantsList: List<Restaurant>) {
        categories = categoriesList
        restaurants = restaurantsList
        filteredRestaurants = restaurants // Initially, all restaurants are displayed
    }

    // Handle category selection
    fun onCategorySelected(category: Category?) {
        selectedCategory = category
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
            val matchesCategory = selectedCategory?.let {
                restaurant.category == it.name
            } ?: true // If no category is selected, show all

            matchesQuery && matchesCategory
        }
    }
}
