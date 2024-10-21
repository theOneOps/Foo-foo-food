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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uds.foufoufood.navigation.AdminNavHost
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.navigation.UserNavHost
import com.uds.foufoufood.view.client.ClientHomeScreen
import com.uds.foufoufood.viewmodel.AdminViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun MainScreen(userViewModel: UserViewModel, navController: NavHostController, adminViewModel: AdminViewModel) {
    // Observer les données utilisateur
    val user by userViewModel.user.observeAsState()
    var connectUser by remember { mutableStateOf<String?>("") }

    // Gérer la mise à jour du rôle utilisateur
    LaunchedEffect(user) {
        user?.let {
            connectUser = it.role
        }
    }

    // Basculer entre AdminNavHost et UserNavHost en fonction du rôle
    when (connectUser) {
        "admin" -> {
            // Si l'utilisateur est admin, afficher AdminNavHost
            AdminNavHost(navController, adminViewModel)
        }
        "client" -> {
            // Si l'utilisateur est un utilisateur classique, afficher UserNavHost
            UserNavHost(navController, userViewModel)
        }
        else -> {
            // Tant que l'utilisateur n'est pas encore connecté ou que le rôle n'est pas défini
            //UserNavHost(navController, userViewModel)
            ClientHomeScreen(navController)
        }
    }
}
