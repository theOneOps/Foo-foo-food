package com.uds.foufoufood.navigation

import AddRestaurantPage
import RestaurantPage
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.ui.component.BottomNavBarAdmin
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
    menuViewModel: MenuViewModel,
    showAdminBottomBar: Boolean // Nouveau paramètre pour conditionner la BottomBar
) {
    var selectedItem by remember { mutableStateOf(0) }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedItem = when (destination.route) {
                Screen.AdminClient.route -> 0
                Screen.AdminLivreur.route -> 1
                Screen.AdminGerant.route -> 2
                Screen.AdminRestaurant.route -> 3
                else -> selectedItem // Ne change pas si ce n'est pas une des routes admin
            }
        }
    }
    Scaffold(
        bottomBar = {
            if (showAdminBottomBar) {
                BottomNavBarAdmin(selectedItem = selectedItem) { index ->
                    selectedItem = index // Met à jour l'élément sélectionné
                    when (index) {
                        0 -> navController.navigate(Screen.AdminClient.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        1 -> navController.navigate(Screen.AdminLivreur.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        2 -> navController.navigate(Screen.AdminGerant.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        3 -> navController.navigate(Screen.AdminRestaurant.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = getStartDestination(connectUser),
            modifier = Modifier.padding(innerPadding)
        ) {
            addAuthGraph(navController, userViewModel)
            addAdminGraph(navController, adminUsersViewModel, adminRestaurantsViewModel)
            addDeliveryGraph(navController, deliveryViewModel, orderViewModel, userViewModel)
            addConnectedGraph(navController, homeViewModel, menuViewModel, userViewModel)
        }
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

    // Page de profil utilisateur
    composable(
        route = Screen.AdminUserProfile.route,
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
    orderViewModel: OrderViewModel,
    userViewModel: UserViewModel
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
            orderViewModel = orderViewModel,
            userViewModel = userViewModel
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
        Log.d("ConnectedNavHost", "ClientRestaurantAllMenusPage")
        val restaurant = menuViewModel.shared_restaurant.value
        restaurant?.let { theRestaurant ->
            ClientRestaurantScreen(
                navController,
                userViewModel,
                menuViewModel,
                theRestaurant
            )
        } ?: Log.d("ConnectedNavHost", "Restaurant data is null")
    }
}


