package com.uds.foufoufood.repository

import android.util.Log
import com.uds.foufoufood.data_class.request.MenuRequest
import com.uds.foufoufood.data_class.response.MenuResponse
import com.uds.foufoufood.network.MenuApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MenuRepository(private val menuApi: MenuApi) {
    suspend fun getAllMenusByRestaurant(token: String, restaurantId: String): MenuResponse? =
        withContext(Dispatchers.IO)
        {
            try {
                Log.e("MenuRepository", restaurantId)
                val response = menuApi.getAllMenusByRestaurant(token, restaurantId)

                // Vérifier si la réponse est réussie
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        return@withContext responseBody
                    } else {
                        Log.e("MenuRepository", "Réponse vide pour getAllMenus")
                        null
                    }
                } else {
                    // Si la réponse n'est pas réussie
                    Log.e(
                        "MenuRepository",
                        "Erreur d'accès aux menus : ${response.message()} " +
                                "(Code: ${response.code()})"
                    )
                    null
                }
            } catch (e: Exception) {
                // En cas d'exception
                Log.e(
                    "MenuRepository", "Exception lors de l'accès aux menus :" +
                            " ${e.message}", e
                )
                null
            }
        }

    suspend fun getAllMenus(): MenuResponse? = withContext(Dispatchers.IO) {
        try {
            val response = menuApi.getAllMenus()
            // Vérifier si la réponse est réussie
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    return@withContext responseBody
                } else {
                    Log.e("MenuRepository", "Réponse vide pour getAllMenus")
                    null
                }
            } else {
                // Si la réponse n'est pas réussie
                Log.e(
                    "MenuRepository",
                    "Erreur d'accès aux menus : ${response.message()} " +
                            "(Code: ${response.code()})"
                )
                null
            }
        } catch (e: Exception) {
            // En cas d'exception
            Log.e(
                "MenuRepository", "Exception lors de l'accès aux menus :" +
                        " ${e.message}", e
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
        image: String
    ): MenuResponse? = withContext(Dispatchers.IO) {
        try {
            // Création de l'objet MenuRequest
            val request = MenuRequest(
                name = name,
                description = description,
                price = price,
                restaurantId = restaurantId,
                category = category,
                image = image
            )

            // Appel à l'API
            val response = menuApi.createMenu(token, request)

            // Vérification si la réponse est réussie
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    return@withContext responseBody
                } else {
                    Log.e("MenuRepository", "Réponse vide lors de la création du menu")
                    null
                }
            } else {
                // Si la réponse n'est pas réussie, loguer l'erreur avec plus de détails
                Log.e(
                    "MenuRepository",
                    "Erreur lors de la création du menu : ${response.message()}" +
                            " (Code: ${response.code()})"
                )
                null
            }
        } catch (e: Exception) {
            // Gestion des exceptions et log d'erreur détaillé
            Log.e(
                "MenuRepository", "Exception lors de la création du menu :" +
                        " ${e.message}", e
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
        image: String
    ): MenuResponse? = withContext(Dispatchers.IO) {
        try {
            // Création de l'objet MenuRequest
            // Appel à l'API
            val request = MenuRequest(
                name = name,
                price = price,
                description = description,
                category = category,
                restaurantId = restaurantId,
                image = image
            )
            val response = menuApi.updateMenu(token, menuId, request)

            // Vérification si la réponse est réussie
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    return@withContext responseBody
                } else {
                    Log.e(
                        "MenuRepository", "Réponse vide " +
                                "lors de la modification du menu"
                    )
                    null
                }
            } else {
                // Si la réponse n'est pas réussie, loguer l'erreur avec plus de détails
                Log.e(
                    "MenuRepository",
                    "Erreur lors de la modification du menu : ${response.message()}" +
                            " (Code: ${response.code()})"
                )
                null
            }
        } catch (e: Exception) {
            // Gestion des exceptions et log d'erreur détaillé
            Log.e(
                "MenuRepository", "Exception lors " +
                        "de la modification du menu : ${e.message}", e
            )
            null
        }
    }


    suspend fun deleteMenu(
        token: String,
        menuId: String
    ): MenuResponse? = withContext(Dispatchers.IO) {
        try {
            // Création de l'objet MenuRequest
            // Appel à l'API
            val response = menuApi.deleteMenu(token, menuId)
            // Vérification si la réponse est réussie
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    return@withContext responseBody
                } else {
                    Log.e(
                        "MenuRepository", "Réponse vide " +
                                "lors de la suppression du menu"
                    )
                    null
                }
            } else {
                // Si la réponse n'est pas réussie, loguer l'erreur avec plus de détails
                Log.e(
                    "MenuRepository",
                    "Erreur lors de la suppression du menu : ${response.message()}" +
                            " (Code: ${response.code()})"
                )
                null
            }
        } catch (e: Exception) {
            // Gestion des exceptions et log d'erreur détaillé
            Log.e(
                "MenuRepository", "Exception lors " +
                        "de la suppression du menu : ${e.message}", e
            )
            null
        }
    }
}
