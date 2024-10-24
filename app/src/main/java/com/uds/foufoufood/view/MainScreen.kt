package com.uds.foufoufood.view

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.navigation.AdminNavHost
import com.uds.foufoufood.navigation.AuthNavHost
import com.uds.foufoufood.navigation.ConnectedNavHost
import com.uds.foufoufood.navigation.DeliveryNavHost
import com.uds.foufoufood.navigation.UnifiedNavHost
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.view.client.ClientRestaurantScreen

import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun MainScreen(
    userViewModel: UserViewModel,
    navController: NavHostController,
    adminUsersViewModel: AdminUsersViewModel,
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel,
    homeViewModel: HomeViewModel,
    menuViewModel: MenuViewModel
) {
    val context = LocalContext.current
    val user by userViewModel.user.observeAsState()
    val isLoading by userViewModel.loading.observeAsState()
    var connectUser by remember { mutableStateOf<String?>("") }

    LaunchedEffect(user) {
        val token = getToken(context)
        if (token != null) {
            userViewModel.getUserFromToken(token)
        }

        user?.let {
            connectUser = it.role
        }

        Log.d("MainScreen", "user: $user")
        Log.d("MainScreen", "connectUser: $connectUser")
    }

    connectUser?.let {
        UnifiedNavHost(
        navController = navController,
        connectUser = it,
        userViewModel = userViewModel,
        adminUsersViewModel = adminUsersViewModel,
        adminRestaurantsViewModel = adminRestaurantsViewModel,
        deliveryViewModel = deliveryViewModel,
        orderViewModel = orderViewModel,
        homeViewModel = homeViewModel,
        menuViewModel = menuViewModel
    )
    }
}