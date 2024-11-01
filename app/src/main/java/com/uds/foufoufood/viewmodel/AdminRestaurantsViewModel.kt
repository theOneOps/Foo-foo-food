package com.uds.foufoufood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.repository.RestaurantRepository
import kotlinx.coroutines.launch

class AdminRestaurantsViewModel(private val restaurantRepository: RestaurantRepository) :
    ViewModel() {
    private val _restaurants = MutableLiveData<List<Restaurant>?>()
    val restaurants: LiveData<List<Restaurant>?> get() = _restaurants

    private val _selected_Restorer = MutableLiveData<Restaurant>()
    val selected_Restorer: LiveData<Restaurant> get() = _selected_Restorer

    fun setSelectedRestaurant(restaurant: Restaurant) {
        _selected_Restorer.value = restaurant
    }

    fun fetchRestaurants() {
        viewModelScope.launch {
            try {
                val fetchedRestaurants = restaurantRepository.getAllRestaurants()
                _restaurants.value = fetchedRestaurants
            } catch (e: Exception) {
                Log.e(
                    "AdminRestaurantsViewModel fetchRestaurants",
                    "Failed to fetch restaurants: ${e.message}"
                )
            }
        }
    }

    fun addRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            try {
                val response = restaurantRepository.createRestaurant(restaurant)
                if (response != null) {
                    if (response.success) {
                        val currentRestaurants =
                            _restaurants.value?.toMutableList() ?: mutableListOf()
                        currentRestaurants.add(restaurant)
                        _restaurants.value = currentRestaurants.toList()
                    } else {
                        Log.e(
                            "AdminRestaurantsViewModel addRestaurant",
                            "Response from addRestaurant null"
                        )
                    }
                } else {
                    Log.e(
                        "AdminRestaurantsViewModel addRestaurant",
                        "Response from addRestaurant null"
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "AdminRestaurantsViewModel addRestaurant",
                    "Exception during addRestaurant: ${e.message}"
                )
            }
        }
    }

    fun deleteRestaurant(restaurantId: String) {
        viewModelScope.launch {
            try {
                val response = restaurantRepository.deleteRestaurant(restaurantId)
                if (response != null) {
                    if (response.success) {
                        val currentRestaurants =
                            _restaurants.value?.toMutableList() ?: mutableListOf()
                        currentRestaurants.removeIf { it._id == restaurantId }
                        _restaurants.value = currentRestaurants
                    } else Log.e(
                        "AdminRestaurantsViewModel deleteRestaurant",
                        "Response from deleteRestaurant null"
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "AdminRestaurantsViewModel deleteRestaurant",
                    "Exception during deleteRestaurant: ${e.message}"
                )

            }
        }
    }

    fun linkARestorer(id: String, restaurantId: String) {
        viewModelScope.launch {
            try {
                val response = restaurantRepository.linkARestorer(id, restaurantId)
                if (response != null) {
                    if (response.success) {
                        response.message?.let {
                            Log.e(
                                "AdminRestaurantsViewModel linkARestorer", "message: $it"
                            )
                        }
                    } else Log.e(
                        "AdminRestaurantsViewModel linkARestorer",
                        "response from linkARestorer null"
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "AdminRestaurantsViewModel linkARestorer",
                    "Exception during linkARestorer: ${e.message}"
                )
            }
        }
    }
}