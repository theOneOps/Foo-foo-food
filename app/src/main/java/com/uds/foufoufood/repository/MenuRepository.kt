package com.uds.foufoufood.repository

import android.util.Log
import com.uds.foufoufood.data_class.request.MenuRequest
import com.uds.foufoufood.data_class.response.MenuResponse
import com.uds.foufoufood.network.MenuApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MenuRepository(private val menuApi: MenuApi) {
    suspend fun getAllMenusByRestaurant(token: String, restaurantId: String): MenuResponse? =
        withContext(Dispatchers.IO) {
            try {
                Log.e("MenuRepository getAllMenusByRestaurant", restaurantId)
                val response = menuApi.getAllMenusByRestaurant(token, restaurantId)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        return@withContext responseBody
                    } else {
                        Log.e("MenuRepository getAllMenusByRestaurant", "Response body is null")
                        null
                    }
                } else {
                    Log.e(
                        "MenuRepository getAllMenusByRestaurant",
                        "Error getting menus by restaurant: ${response.message()} (Code: ${response.code()})"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.e(
                    "MenuRepository getAllMenusByRestaurant",
                    "Exception getting menus by restaurant: ${e.message}",
                    e
                )
                null
            }
        }

    suspend fun getAllMenus(): MenuResponse? = withContext(Dispatchers.IO) {
        try {
            val response = menuApi.getAllMenus()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    return@withContext responseBody
                } else {
                    Log.e("MenuRepository getAllMenus", "Response body is null")
                    null
                }
            } else {
                Log.e(
                    "MenuRepository getAllMenus",
                    "Error getting all menus: ${response.message()} (Code: ${response.code()}"
                )
                null
            }
        } catch (e: Exception) {
            Log.e(
                "MenuRepository getAllMenus", "Exception getting all menus: ${e.message}", e
            )
            null
        }
    }

    suspend fun createMenu(
        token: String,
        name: String,
        description: String,
        price: Double,
        restaurantId: String,
        category: String,
        image: String,
        ingredients: List<String>
    ): MenuResponse? = withContext(Dispatchers.IO) {
        try {
            val request = MenuRequest(
                name = name,
                description = description,
                price = price,
                restaurantId = restaurantId,
                category = category,
                image = image,
                ingredients = ingredients
            )

            val response = menuApi.createMenu(token, request)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    return@withContext responseBody
                } else {
                    Log.e("MenuRepository createMenu", "Response body is null")
                    null
                }
            } else {
                Log.e(
                    "MenuRepository createMenu",
                    "Error creating menu: ${response.message()} (Code: ${response.code()})"
                )
                null
            }
        } catch (e: Exception) {
            Log.e(
                "MenuRepository createMenu", "Exception creating menu: ${e.message}", e
            )
            null
        }
    }

    suspend fun updateMenu(
        token: String,
        menuId: String,
        name: String,
        description: String,
        price: Double,
        category: String,
        restaurantId: String,
        image: String,
        ingredients: List<String>
    ): MenuResponse? = withContext(Dispatchers.IO) {
        try {
            val request = MenuRequest(
                name = name,
                price = price,
                description = description,
                category = category,
                restaurantId = restaurantId,
                image = image,
                ingredients = ingredients
            )
            val response = menuApi.updateMenu(token, menuId, request)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    return@withContext responseBody
                } else {
                    Log.e(
                        "MenuRepository updateMenu", "Response body is null"
                    )
                    null
                }
            } else {
                Log.e(
                    "MenuRepository updateMenu",
                    "Error updating menu: ${response.message()} (Code: ${response.code()})"
                )
                null
            }
        } catch (e: Exception) {
            Log.e(
                "MenuRepository updateMenu", "Exception updating menu: ${e.message}", e
            )
            null
        }
    }

    suspend fun deleteMenu(
        token: String, menuId: String
    ): MenuResponse? = withContext(Dispatchers.IO) {
        try {
            val response = menuApi.deleteMenu(token, menuId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    return@withContext responseBody
                } else {
                    Log.e(
                        "MenuRepository deleteMenu", "Response body is null"
                    )
                    null
                }
            } else {
                Log.e(
                    "MenuRepository deleteMenu",
                    "Error deleting menu: ${response.message()} (Code: ${response.code()})"
                )
                null
            }
        } catch (e: Exception) {
            Log.e(
                "MenuRepository deleteMenu", "Exception deleting menu: ${e.message}", e
            )
            null
        }
    }
}