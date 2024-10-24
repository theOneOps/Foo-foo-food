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
            address = Address("Address 1"),
            speciality = "Speciality 1",
            phone = "Phone 1",
            openingHours = "Opening Hours 1",
            items = listOf(),
            rating = 4.5,
            reviews = listOf(),
            imageUrl = "https://source.unsplash.com/random/200x200",
            category = "Category 1"
        ),
        Restaurant(
            name = "Restaurant 2",
            address = Address("Address 1"),
            speciality = "Speciality 2",
            phone = "Phone 2",
            openingHours = "Opening Hours 2",
            items = listOf(),
            rating = 4.0,
            reviews = listOf(),
            imageUrl = "https://source.unsplash.com/random/200x200",
            category = "Category 2"
        ),
        Restaurant(
            name = "Restaurant 3",
            address = Address("Address 1"),
            speciality = "Speciality 3",
            phone = "Phone 3",
            openingHours = "Opening Hours 3",
            items = listOf(),
            rating = 3.5,
            reviews = listOf(),
            imageUrl = "https://source.unsplash.com/random/200x200",
            category = "Category 3"
        ),
        Restaurant(
            name = "Restaurant 4",
            address = Address("Address 1"),
            speciality = "Speciality 4",
            phone = "Phone 4",
            openingHours = "Opening Hours 4",
            items = listOf(),
            rating = 3.0,
            reviews = listOf(),
            imageUrl = "https://source.unsplash.com/random/200x200",
            category = "Category 4"
        ),
        Restaurant(
            name = "Restaurant 5",
            address = Address("Address 1"),
            speciality = "Speciality 5",
            phone = "Phone 5",
            openingHours = "Opening Hours 5",
            items = listOf(),
            rating = 2.5,
            reviews = listOf(),
            imageUrl = "https://source.unsplash.com/random/200x200",
            category = "Category 5"
        )
    )
}