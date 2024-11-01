package com.uds.foufoufood.navigation

// This class is used to define the different screens of the application
sealed class Screen(val route: String) {
    // AUTH
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")
    object Address : Screen("address")
    object UpdateAddress : Screen("update_address")

    // HOME
    object HomeRestaurant : Screen("home_restaurant")
    object HomeMenu : Screen("home_menu")

    // ADMIN
    object AdminClient : Screen("admin_client")
    object AdminLivreur : Screen("admin_livreur")
    object AdminGerant : Screen("admin_gerant")
    object AdminRestaurant : Screen("admin_restaurant")
    object AdminUserProfile : Screen("userProfile/{userEmail}")
    object AdminLinkARestorerToAResto : Screen("admin_linked_restaurant_to_restorer")

    // DELIVERY
    object DeliveryAvailablePage : Screen("delivery_available_page")
    object DeliveryOrderDetailsPage : Screen("delivery_order_page")
    object DeliveryAllOrdersPage : Screen("delivery_all_orders_page")
    object OrderTracking : Screen("orderTracking")

    // CLIENT
    object ClientRestaurantAllMenusPage : Screen("client_restaurant_all_menu_page")
    object ClientInstanceMenuPage : Screen("menu_page")
    object Cart : Screen("cart")
    object Notifications : Screen("notifications")

    // RESTAURANT
    object ModifyRestaurantPage : Screen("modify_restaurant_page")
}