package com.uds.foufoufood.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uds.foufoufood.view.HomeScreen
import com.uds.foufoufood.view.auth.VerifyCodeScreen
import com.uds.foufoufood.view.auth.WelcomeScreen
import com.uds.foufoufood.view.client.AddressScreen
import com.uds.foufoufood.view.client.ProfileScreen
import com.uds.foufoufood.view.client.UpdateAddressScreen
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
                HomeScreen(navController, homeViewModel, userViewModel)
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        composable(
            route = "verify_code/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            VerifyCodeScreen(
                navController = navController,
                userViewModel = userViewModel,
                email = email ?: ""
            )
        }

        composable(Screen.Address.route) {
            AddressScreen(navController, userViewModel)
        }

        composable(Screen.UpdateAddress.route) {
            UpdateAddressScreen(navController, userViewModel)
        }
    }
}
