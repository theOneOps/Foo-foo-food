package com.uds.foufoufood.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uds.foufoufood.view.HomeScreen
import com.uds.foufoufood.view.client.ClientRestaurantScreen
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.UserViewModel


@Composable
fun ConnectedNavHost(
    navController: NavHostController,
    userViewModel: UserViewModel,
    homeViewModel: HomeViewModel,
    menuViewModel: MenuViewModel
) {
//    Log.d("UserNavHost", "UserNavHost")
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            if (userViewModel.user.value?.role == "client" ||
                userViewModel.user.value?.role == "restaurateur" ||
                userViewModel.user.value?.role == "livreur"
            ) {
                HomeScreen(navController, homeViewModel, menuViewModel)
            }
        }

        composable(Screen.ClientRestaurantAllMenusPage.route)
        {
            if (userViewModel.user.value?.role == "client" ||
                userViewModel.user.value?.role == "restaurateur" ||
                userViewModel.user.value?.role == "livreur"
            )
                menuViewModel.shared_restaurant.value?.let { theRestaurant ->
                    ClientRestaurantScreen(
                        navController,
                        userViewModel,
                        menuViewModel,
                        theRestaurant
                    )
                }
        }
    }


}
