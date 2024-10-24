package com.uds.foufoufood.navigation

import android.util.Log
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
import com.uds.foufoufood.view.client.ProfileScreen
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.UserViewModel


@Composable
fun AuthNavHost(
    navController: NavHostController,
    userViewModel: UserViewModel,
    homeViewModel: HomeViewModel,
    menuViewModel: MenuViewModel
) {
    Log.d("UserNavHost", "UserNavHost")
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            if (userViewModel.user.value?.role == "" ||
                userViewModel.user.value?.role == null
            )
                WelcomeScreen(navController)
        }
        composable(Screen.Login.route) {
            if (userViewModel.user.value?.role == "" ||
                userViewModel.user.value?.role == null
            )
                LoginScreen(navController, userViewModel)
        }

        composable(Screen.Register.route) {
            if (userViewModel.user.value?.role == "" ||
                userViewModel.user.value?.role == null
            )
                RegisterFirstPartScreen(navController, userViewModel)
        }

        composable(
            route = "verify_code/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (userViewModel.user.value?.role == "" ||
                userViewModel.user.value?.role == null
            )
                VerifyCodeScreen(
                    navController = navController,
                    userViewModel = userViewModel,
                    email = email ?: ""
                )
        }

        composable(
            route = "define_profile/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (userViewModel.user.value?.role == "" ||
                userViewModel.user.value?.role == null
            )
                DefineProfileScreen(
                    navController = navController,
                    userViewModel = userViewModel,
                    email = email ?: ""
                )
        }

//        composable(Screen.Home.route) {
//            if (userViewModel.user.value?.role == "client" ||
//                userViewModel.user.value?.role == "restaurateur" ||
//                userViewModel.user.value?.role == "livreur"
//            )
//            HomeScreen(navController, homeViewModel, menuViewModel)
//        }


    }
}
