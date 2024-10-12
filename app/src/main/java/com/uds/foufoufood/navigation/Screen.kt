package com.uds.foufoufood.navigation

// Classe scellée pour définir les routes de navigation
sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AdminHome : Screen("admin_home")
}