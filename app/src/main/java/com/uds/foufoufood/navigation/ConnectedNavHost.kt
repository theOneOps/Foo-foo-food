package com.uds.foufoufood.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.uds.foufoufood.view.HomeScreen
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.UserViewModel


@Composable
fun ConnectedNavHost(
    navController: NavHostController,
    userViewModel: UserViewModel,
    homeViewModel: HomeViewModel
) {
    Log.d("UserNavHost", "UserNavHost")
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            if (userViewModel.user.value?.role == "client")
                HomeScreen(navController, homeViewModel)
        }
    }
}
