package com.uds.foufoufood.navigation

import AddRestaurantPage
import RestaurantPage
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.ui.component.BottomNavBarAdmin
import com.uds.foufoufood.ui.component.BottomNavBarClient
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
import com.uds.foufoufood.view.client.CartScreen
import com.uds.foufoufood.view.client.ClientRestaurantScreen
import com.uds.foufoufood.view.client.MenuRestaurantScreen
import com.uds.foufoufood.view.client.OrderTrackingScreen
import com.uds.foufoufood.view.client.ProfileScreen
import com.uds.foufoufood.view.client.UpdateAddressScreen
import com.uds.foufoufood.view.delivery.AllOrdersScreen
import com.uds.foufoufood.view.delivery.AvailabilityScreen
import com.uds.foufoufood.view.delivery.DeliveryOrderScreen
import com.uds.foufoufood.view.restorer.FormModifyRestaurantScreen
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.CartViewModel
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.OrderTrackingViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun UnifiedNavHost(
    navController: NavHostController,
    connectUser: String, // Role or empty if not connected
    emailValidated: Boolean,
    userViewModel: UserViewModel,
    adminUsersViewModel: AdminUsersViewModel,
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel,
    homeViewModel: HomeViewModel,
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel,
    orderTrackingViewModel: OrderTrackingViewModel,
    showAdminBottomBar: Boolean
) {
    var selectedItem by remember { mutableStateOf(0) }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedItem = when (destination.route) {
                Screen.Cart.route -> 0
                //Screen.Notifications.route -> 1
                Screen.AdminClient.route -> 0
                Screen.AdminLivreur.route -> 1
                Screen.AdminGerant.route -> 2
                Screen.AdminRestaurant.route -> 3
                else -> selectedItem
            }
        }
    }

    Scaffold(
        // Show the bottom bar only if the user is connected and the role is set
        bottomBar = {
            if (emailValidated && connectUser.isNotEmpty()) { // Only show when connected
                if (connectUser == "admin" && showAdminBottomBar) {
                    BottomNavBarAdmin(selectedItem = selectedItem) { index ->
                        selectedItem = index
                        when (index) {
                            0 -> navController.navigate(Screen.AdminClient.route)
                            1 -> navController.navigate(Screen.AdminLivreur.route)
                            2 -> navController.navigate(Screen.AdminGerant.route)
                            3 -> navController.navigate(Screen.AdminRestaurant.route)
                        }
                    }
                } else {
                    BottomNavBarClient(
                        selectedItem = selectedItem,
                        onItemSelected = { index -> selectedItem = index },
                        navController = navController
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = getStartDestination(connectUser, emailValidated),
            modifier = Modifier.padding(innerPadding)
        ) {
            addAuthGraph(navController, userViewModel)
            addAdminGraph(
                navController,
                adminUsersViewModel,
                adminRestaurantsViewModel,
                userViewModel
            )
            addDeliveryGraph(navController, deliveryViewModel, orderViewModel, userViewModel)
            addConnectedGraph(
                navController,
                homeViewModel,
                menuViewModel,
                userViewModel,
                cartViewModel,
                orderTrackingViewModel
            )
        }
    }
}


fun getStartDestination(connectUser: String, emailValidated: Boolean): String {
    if (!emailValidated) {
        return Screen.Welcome.route
    }
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
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    userViewModel: UserViewModel
) {

    composable(Screen.AdminClient.route) {
        ClientScreen(
            navController = navController,
            adminUsersViewModel = adminUsersViewModel,
            userViewModel = userViewModel,
            onRoleChanged = { user, newRole ->
                adminUsersViewModel.updateUserRole(user, newRole)
            }
        )
    }
    composable(Screen.AdminLivreur.route) {
        LivreurPage(
            navController = navController,
            adminUsersViewModel = adminUsersViewModel,
            userViewModel = userViewModel,
            onRoleChanged = { user, newRole ->
                adminUsersViewModel.updateUserRole(user, newRole)
            }
        )
    }
    composable(Screen.AdminGerant.route) {
        GerantPage(
            navController = navController,
            adminUsersViewModel = adminUsersViewModel,
            userViewModel = userViewModel,
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
            restaurants = adminRestaurantsViewModel.restaurants,
            userViewModel = userViewModel
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
        AvailabilityScreen(
            navController = navController,
            deliveryViewModel = deliveryViewModel,
            orderViewModel = orderViewModel,
            userViewModel = userViewModel
        )
    }

    composable(Screen.DeliveryOrderDetailsPage.route) {
        DeliveryOrderScreen(
            navController = navController,
            orderViewModel = orderViewModel,
            userViewModel = userViewModel
        )
    }

    composable(Screen.DeliveryAllOrdersPage.route) {
        AllOrdersScreen(
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
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel,
    orderTrackingViewModel: OrderTrackingViewModel
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
        val restaurant by menuViewModel.shared_restaurant.observeAsState()
        restaurant?.let { theRestaurant ->
            ClientRestaurantScreen(
                navController,
                menuViewModel,
                theRestaurant
            )
        } ?: Log.d("ConnectedNavHost", "Restaurant data is null")
    }

    composable(Screen.ModifyRestaurantPage.route) {
        Log.d("ConnectedNavHost", Screen.ModifyRestaurantPage.route)
        if (userViewModel.user.value?.role == "restaurateur"
        ) {
            val restaurant by menuViewModel.shared_restaurant.observeAsState()
            restaurant?.let { theRestaurant ->
                FormModifyRestaurantScreen(theRestaurant, homeViewModel)
            } ?: Log.d("ConnectedNavHost", "Menu data is null")
        } else {
            Log.d("ConnectedNavHost", "Invalid role or no menu selected")
        }
    }

    composable(Screen.ClientInstanceMenuPage.route)
    {
        Log.d("ConnectedNavHost", Screen.ClientInstanceMenuPage.route)
        if (userViewModel.user.value?.role == "client" ||
            userViewModel.user.value?.role == "restaurateur"
        ) {
            val menu by menuViewModel.shared_current_menu.observeAsState()
            menu?.let { selectMenu ->
                MenuRestaurantScreen(selectMenu, menuViewModel, cartViewModel)
            } ?: Log.d("ConnectedNavHost", "Menu data is null")
        } else {
            Log.d("ConnectedNavHost", "Invalid role or no menu selected")
        }
    }

    composable(Screen.Cart.route) {
        val user = userViewModel.user.value
        if (user != null) {
            CartScreen(
                cartViewModel = cartViewModel
            )
        }
    }

    composable(Screen.OrderTracking.route) {
        val user = userViewModel.user.value
        if (user != null) {
            OrderTrackingScreen(orderTrackingViewModel = orderTrackingViewModel)
        }
    }

}


