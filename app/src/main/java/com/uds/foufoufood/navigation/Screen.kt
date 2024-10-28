package com.uds.foufoufood.navigation

// Classe scellée pour définir les routes de navigation
sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Address : Screen("address")
    object UpdateAddress : Screen("update_address")

    //Admin
    object AdminClient : Screen("admin_client")
    object AdminLivreur : Screen("admin_livreur")
    object AdminGerant : Screen("admin_gerant")
    object AdminRestaurant : Screen("admin_restaurant")
    object AdminUserProfile : Screen("userProfile/{userEmail}") {
        fun createRoute(userEmail: String): String {
            return "userProfile/$userEmail"
        }
    }

    // Delivery
    object DeliveryAvailablePage : Screen("delivery_available_page")
    object DeliveryOrderDetailsPage : Screen("delivery_order_page")
    object DeliveryAllOrdersPage : Screen("delivery_all_orders_page")
    object OrderTracking : Screen("orderTracking")

    // Client
    object ClientRestaurantAllMenusPage : Screen("client_restaurant_all_menu_page")
    object ClientInstanceMenuPage:Screen("menu_page")
    object Cart:Screen("cart")
    // Restaurateur
    object ModifyRestaurantPage:Screen("modify_restaurant_page")


}