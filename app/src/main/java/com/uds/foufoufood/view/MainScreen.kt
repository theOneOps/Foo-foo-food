package com.uds.foufoufood.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.navigation.UnifiedNavHost
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.CartViewModel
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.RestaurantViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun MainScreen(
    userViewModel: UserViewModel,
    navController: NavHostController,
    adminUsersViewModel: AdminUsersViewModel,
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel,
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel,
    restaurantViewModel: RestaurantViewModel
) {
    val context = LocalContext.current
    val user by userViewModel.user.observeAsState()
    val isLoading by userViewModel.loading.observeAsState()
    var emailValidated by remember { mutableStateOf(false) }
    var connectUser by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        // Au démarrage de l'application, vérifiez si un token est disponible
        val token = getToken(context)
        Log.d("MainScreen", "Token: $token")
        if (token != null) {
            userViewModel.getUserFromToken(token)
        }
    }

    // Met à jour connectUser dès que l'utilisateur est disponible
    user?.let {
        connectUser = it.role ?: ""
        emailValidated = it.emailValidated ?: false
    }



    Log.d("MainScreen", "connectUser: $connectUser, isLoading: $isLoading, user: $user")


    if (user == null) {
        connectUser = ""
        emailValidated = false
    }
    // Détermine si la BottomBar doit être affichée pour l'admin
    val showAdminBottomBar = connectUser == "admin"


    if (isLoading == true) {
        LoadingScreen()
    }

    UnifiedNavHost(
        navController = navController,
        connectUser = connectUser,
        emailValidated = emailValidated,
        userViewModel = userViewModel,
        adminUsersViewModel = adminUsersViewModel,
        adminRestaurantsViewModel = adminRestaurantsViewModel,
        deliveryViewModel = deliveryViewModel,
        orderViewModel = orderViewModel,
        menuViewModel = menuViewModel,
        restaurantViewModel = restaurantViewModel,
        cartViewModel = cartViewModel,
        showAdminBottomBar = showAdminBottomBar
    )
}