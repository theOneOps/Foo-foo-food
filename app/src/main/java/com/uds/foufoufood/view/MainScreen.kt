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
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun MainScreen(
    userViewModel: UserViewModel,
    navController: NavHostController,
    adminUsersViewModel: AdminUsersViewModel,
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    deliveryViewModel: DeliveryViewModel,
    homeViewModel: HomeViewModel
) {
    // Observer les données utilisateur
    val user by userViewModel.user.observeAsState()
    var connectUser by remember { mutableStateOf<String?>("") }
    Log.d("MainScreen", "User: $user")

    // Gérer la mise à jour du rôle utilisateur
    LaunchedEffect(user) {
        user?.let {
            connectUser = it.role
        }
    }

    Log.d("MainScreen", "ConnectUser: $connectUser")

    // Basculer entre AdminNavHost et UserNavHost en fonction du rôle

    if (connectUser == "admin") {
        // Si l'utilisateur est admin, afficher AdminNavHost
        AdminNavHost(navController, adminUsersViewModel, adminRestaurantsViewModel)

    } else if (connectUser == "client") {
        Log.d("MainScreen", "Client")
        ConnectedNavHost(navController, userViewModel, homeViewModel)

    } else if (connectUser == "livreur") {
        Log.d("MainScreen", "Livreur")
        DeliveryNavHost(navController, deliveryViewModel)
    }
    else {
        // Tant que l'utilisateur n'est pas encore connecté ou que le rôle n'est pas défini
        AuthNavHost(navController, userViewModel, homeViewModel)
        //ClientHomeScreen(navController)
    }
}