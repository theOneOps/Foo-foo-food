package com.uds.foufoufood.view

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.uds.foufoufood.navigation.AdminNavHost
import com.uds.foufoufood.navigation.AuthNavHost
import com.uds.foufoufood.navigation.ConnectedNavHost
import com.uds.foufoufood.navigation.DeliveryNavHost
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
    // Observer les données utilisateur
    val user by userViewModel.user.observeAsState()
    var connectUser by remember { mutableStateOf<String?>("") }
    Log.d("MainScreen", "User: $user")
    Log.d("MainScreen", "ConnectUser: $connectUser")

    // Gérer la mise à jour du rôle utilisateur
    LaunchedEffect(user) {
        user?.let {
            connectUser = it.role
        }
        if (user == null) {
            connectUser = ""
        }
    }

    Log.d("MainScreen", "ConnectUser: $connectUser")

    // Basculer entre AdminNavHost et UserNavHost en fonction du rôle
    if (connectUser == "admin")
    {
        // Si l'utilisateur est admin, afficher AdminNavHost
        AdminNavHost(navController, adminUsersViewModel, adminRestaurantsViewModel)

    } else if (connectUser == "client"
        || connectUser == "restaurateur")
    {
        // Si l'utilisateur est un utilisateur classique, afficher UserNavHost
        ConnectedNavHost(navController, userViewModel, homeViewModel)

    } else if (connectUser == "livreur") {
        Log.d("MainScreen", "Livreur")
        DeliveryNavHost(navController, deliveryViewModel, orderViewModel)
    }
    else {
        // Tant que l'utilisateur n'est pas encore connecté ou que le rôle n'est pas défini
        AuthNavHost(navController, userViewModel, homeViewModel, menuViewModel)
        //ClientRestaurantScreen(navController, userViewModel, menuViewModel)
    }
}