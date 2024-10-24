package com.uds.foufoufood.navigation

import AddRestaurantPage
import RestaurantPage
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.view.HomeScreen
import com.uds.foufoufood.view.admin.ClientScreen
import com.uds.foufoufood.view.admin.GerantPage
import com.uds.foufoufood.view.admin.LivreurPage
import com.uds.foufoufood.view.admin.UserProfileAdminPage
import com.uds.foufoufood.view.auth.DefineProfileScreen
import com.uds.foufoufood.view.auth.LoginScreen
import com.uds.foufoufood.view.auth.RegisterFirstPartScreen
import com.uds.foufoufood.view.auth.VerifyCodeScreen
import com.uds.foufoufood.view.auth.WelcomeScreen
import com.uds.foufoufood.view.client.AddressScreen
import com.uds.foufoufood.view.client.ClientRestaurantScreen
import com.uds.foufoufood.view.client.ProfileScreen
import com.uds.foufoufood.view.client.UpdateAddressScreen
import com.uds.foufoufood.view.delivery.AvailabilityScreen
import com.uds.foufoufood.view.delivery.DeliveryOrderScreen
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun UnifiedNavHost(
    navController: NavHostController,
    connectUser: String,
    userViewModel: UserViewModel,
    adminUsersViewModel: AdminUsersViewModel,
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel,
    homeViewModel: HomeViewModel,
    menuViewModel: MenuViewModel
) {
    NavHost(navController = navController, startDestination = getStartDestination(connectUser)) {
        addAuthGraph(navController, userViewModel)
        addAdminGraph(navController, adminUsersViewModel, adminRestaurantsViewModel)
        addDeliveryGraph(navController, deliveryViewModel, orderViewModel)
        addConnectedGraph(navController, homeViewModel, menuViewModel, userViewModel)
    }
}

fun getStartDestination(connectUser: String): String {
    return when (connectUser) {
        "admin" -> Screen.AdminClient.route
        "livreur" -> Screen.DeliveryAvailablePage.route
        "client", "restaurateur" -> Screen.Home.route
        else -> Screen.Welcome.route
    }
}

/////////////////////////////AUTH

fun NavGraphBuilder.addAuthGraph(navController: NavHostController, userViewModel: UserViewModel) {
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
        DefineProfileScreen(
            navController = navController,
            userViewModel = userViewModel,
            email = email ?: ""
        )
    }
}


/////////////////////////////ADMIN

fun NavGraphBuilder.addAdminGraph(
    navController: NavHostController,
    adminUsersViewModel: AdminUsersViewModel,
    adminRestaurantsViewModel: AdminRestaurantsViewModel
) {

    composable(Screen.AdminClient.route) {
        ClientScreen(
            navController = navController,
            adminUsersViewModel = adminUsersViewModel,
            onRoleChanged = { user, newRole ->
                adminUsersViewModel.updateUserRole(user, newRole)
            }
        )
    }
    composable(Screen.AdminLivreur.route) {
        LivreurPage(
            navController = navController,
            adminUsersViewModel = adminUsersViewModel,
            onRoleChanged = { user, newRole ->
                adminUsersViewModel.updateUserRole(user, newRole)
            }
        )
    }
    composable(Screen.AdminGerant.route) {
        GerantPage(
            navController = navController,
            adminUsersViewModel = adminUsersViewModel,
            onRoleChanged = { user, newRole ->
                adminUsersViewModel.updateUserRole(user, newRole)
            }
        )
    }

    // Ajouter les routes utilisateur
    composable(Screen.AdminClient.route) {
        UserProfileAdminPage(
            navController = navController,
            userEmail = null,
            users = adminUsersViewModel.getAll(),
            onRoleChanged = { user, newRole ->
                adminUsersViewModel.updateUserRole(user, newRole)
            }
        )
    }

    // Page de profil utilisateur
    composable(
        route = Screen.UserProfile.route,
        arguments = listOf(navArgument("userEmail") { type = NavType.StringType })
    ) { backStackEntry ->
        val userEmail = backStackEntry.arguments?.getString("userEmail")
        UserProfileAdminPage(
            navController = navController,
            userEmail = userEmail,
            users = adminUsersViewModel.getAll(),
            onRoleChanged = { user, newRole ->
                adminUsersViewModel.updateUserRole(user, newRole)
            }
        )
    }

    // Page de gestion des restaurants
    composable(Screen.AdminRestaurant.route) {
        RestaurantPage(
            navController = navController,
            restaurants = adminRestaurantsViewModel.restaurants
        )
    }

    // Page pour ajouter un restaurant
    composable("addRestaurant") {
        AddRestaurantPage(
            navController = navController,
            onRestaurantAdded = { newRestaurant: Restaurant ->
                adminRestaurantsViewModel.addRestaurant(newRestaurant)
            }
        )
    }
}

/////////////////////////////DELIVERY

fun NavGraphBuilder.addDeliveryGraph(
    navController: NavHostController,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel
) {
    composable(Screen.DeliveryAvailablePage.route) {
        AvailabilityScreen (
            navController = navController,
            deliveryViewModel = deliveryViewModel,
            orderViewModel = orderViewModel
        )
    }

    composable(Screen.DeliveryOrderPage.route) {
        DeliveryOrderScreen (
            navController = navController,
            orderViewModel = orderViewModel
        )
    }
}

/////////////////////////////CONNECTED

fun NavGraphBuilder.addConnectedGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    menuViewModel: MenuViewModel,
    userViewModel: UserViewModel
) {
    // Home Screen (accessible pour 'client' et 'restaurateur')
    composable(Screen.Home.route) {
        HomeScreen(navController, homeViewModel, userViewModel, menuViewModel)
    }

    // Welcome Screen
    composable(Screen.Welcome.route) {
        WelcomeScreen(navController)
    }

    // Profile Screen
    composable(Screen.Profile.route) {
        ProfileScreen(
            navController = navController,
            userViewModel = userViewModel
        )
    }

    // Verify Code Screen
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

    // Address Screen
    composable(Screen.Address.route) {
        AddressScreen(navController, userViewModel)
    }

    // Update Address Screen
    composable(Screen.UpdateAddress.route) {
        UpdateAddressScreen(navController, userViewModel)
    }

    // Client Restaurant All Menus Page (accessible pour 'client' et 'restaurateur')
    composable(Screen.ClientRestaurantAllMenusPage.route) {
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


