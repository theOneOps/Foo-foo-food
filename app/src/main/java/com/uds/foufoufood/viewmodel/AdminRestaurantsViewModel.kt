package com.uds.foufoufood.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Restaurant

class AdminRestaurantsViewModel : ViewModel() {
    val restaurants = mutableStateListOf<Restaurant>(*getRestaurants().toTypedArray())

    fun addRestaurant(newRestaurant: Restaurant) {
        restaurants.add(newRestaurant)
    }

    private fun getRestaurants() = listOf(
        Restaurant(
            name = "Restaurant 1",
            address = Address(2),
            speciality = "Speciality 1",
            phone = "Phone 1",
            openingHours = "Opening Hours 1",
            items = listOf(),
            rating = 4.5,
            reviews = listOf(),
            imageUrl = "https://source.unsplash.com/random/200x200",
            restaurantId = "1",
            userId = "1",
        ),
    )
}