package com.uds.foufoufood.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uds.foufoufood.view.auth.DefineProfileScreen
import com.uds.foufoufood.view.HomeScreen
import com.uds.foufoufood.view.auth.LoginScreen
import com.uds.foufoufood.view.auth.RegisterFirstPartScreen
import com.uds.foufoufood.view.auth.VerifyCodeScreen
import com.uds.foufoufood.view.auth.WelcomeScreen
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.UserViewModel


@Composable
fun UserNavHost(navController: NavHostController, userViewModel: UserViewModel) {
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController, userViewModel)
        }

        composable(Screen.Register.route) {
            RegisterFirstPartScreen(navController, userViewModel)
        }

        composable(
            route = "verify_code/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            VerifyCodeScreen(navController = navController, userViewModel = userViewModel, email = email ?: "")
        }

        composable(
            route = "define_profile/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            DefineProfileScreen(navController = navController, userViewModel = userViewModel, email = email ?: "")
        }

        composable(Screen.Home.route) {
            HomeScreen(HomeViewModel())
        }


    }
}
