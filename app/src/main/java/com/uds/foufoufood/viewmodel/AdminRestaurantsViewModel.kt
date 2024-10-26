package com.uds.foufoufood.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.repository.RestaurantRepository
import kotlinx.coroutines.launch

class AdminRestaurantsViewModel(private val restaurantRepository: RestaurantRepository) :
    ViewModel() {
    private val _restaurants = MutableLiveData<List<Restaurant>?>()
    val restaurants: LiveData<List<Restaurant>?> get() = _restaurants


    fun fetchRestaurants() {
        viewModelScope.launch {
            try {
                val fetchedRestaurants = restaurantRepository.getAllRestaurants()
                _restaurants.value = fetchedRestaurants
            } catch (e: Exception) {
                Log.e("AdminRestaurantsViewModel", "Failed to fetch restaurants: ${e.message}")
            }
        }
    }

    fun addRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            try {
                val response = restaurantRepository.createRestaurant(restaurant)
                if (response != null) {
                    if (response.success) {
                        val currentRestaurants = _restaurants.value?.toMutableList() ?: mutableListOf()
                        currentRestaurants.add(restaurant)
                        _restaurants.value = currentRestaurants.toList()
                    } else {
                        Log.e("AdminRestaurantsViewModel", "addRestaurant is not working")
                    }
                } else {
                    Log.e("AdminRestaurantsViewModel", "response from addRestaurant null, problem to fix !")
                }
            } catch (e: Exception) {
                Log.e("AdminRestaurantsViewModel", "Exception during addRestaurant: ${e.message}")
            }
        }
    }



}