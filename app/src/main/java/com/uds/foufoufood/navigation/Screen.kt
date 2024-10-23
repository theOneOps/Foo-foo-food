package com.uds.foufoufood.navigation

// Classe scellée pour définir les routes de navigation
sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")

    //Admin
    object AdminClient : Screen("admin_client")
    object AdminLivreur : Screen("admin_livreur")
    object AdminGerant : Screen("admin_gerant")
    object AdminRestaurant : Screen("admin_restaurant")
    object UserProfile : Screen("userProfile/{userEmail}") {
        fun createRoute(userEmail: String): String {
            return "userProfile/$userEmail"
        }
    }

    // Client
    object ClientRestaurantAllMenusPage : Screen("client_restaurant_all_menu_page")
}