package com.uds.foufoufood.navigation

import AddRestaurantPage
import AvailabilityScreen
import RestaurantPage
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.repository.OrderRepository
import com.uds.foufoufood.ui.component.BottomNavBarAdmin
import com.uds.foufoufood.ui.component.BottomNavBarClient
import com.uds.foufoufood.view.HomeScreenMenu
import com.uds.foufoufood.view.HomeScreenRestaurant
import com.uds.foufoufood.view.admin.ClientScreen
import com.uds.foufoufood.view.admin.GerantPage
import com.uds.foufoufood.view.admin.LinkRestorerRestoAndRestoPage
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
import com.uds.foufoufood.view.client.NotificationsScreen
import com.uds.foufoufood.view.client.OrderTrackingScreen
import com.uds.foufoufood.view.client.ProfileScreen
import com.uds.foufoufood.view.client.UpdateAddressScreen
import com.uds.foufoufood.view.delivery.AllOrdersScreen
import com.uds.foufoufood.view.delivery.DeliveryOrderDetailsScreen
import com.uds.foufoufood.view.restorer.FormModifyRestaurantScreen
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.CartViewModel
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.RestaurantViewModel
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
    menuViewModel: MenuViewModel,
    restaurantViewModel: RestaurantViewModel,
    cartViewModel: CartViewModel,
    orderRepository: OrderRepository,
    showAdminBottomBar: Boolean,
    googleSignInClient: GoogleSignInClient,
    auth: FirebaseAuth
) {
    var selectedItem by remember { mutableStateOf(0) }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedItem = when (destination.route) {

                Screen.AdminClient.route -> 0
                Screen.AdminLivreur.route -> 1
                Screen.AdminGerant.route -> 2
                Screen.AdminRestaurant.route -> 3
                else -> selectedItem
            }
        }
    }
    Scaffold(
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
                } else if (connectUser == "client" || connectUser == "restaurateur") {
                    BottomNavBarClient(
                        selectedItem = selectedItem,
                        userViewModel = userViewModel,
                        onclick = { index ->
                            selectedItem = index
                            when (index) {
                                0 -> navController.navigate(Screen.HomeRestaurant.route)
                                1 -> navController.navigate(Screen.HomeMenu.route)
                                2 -> navController.navigate(Screen.Cart.route)
                                3 -> navController.navigate(Screen.Notifications.route)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = getStartDestination(connectUser, emailValidated),
            modifier = Modifier.padding(innerPadding).background(color = colorResource(id = R.color.white)),
            enterTransition = { fadeIn(animationSpec = tween(700)) + scaleIn(initialScale = 0.95f) },
            exitTransition = { fadeOut(animationSpec = tween(700)) + scaleOut(targetScale = 1.05f) }
        ) {
            addAuthGraph(navController, userViewModel, googleSignInClient, auth)
            addAdminGraph(
                navController,
                adminUsersViewModel,
                adminRestaurantsViewModel,
                userViewModel
            )
            addDeliveryGraph(navController, deliveryViewModel, orderViewModel, userViewModel)
            addConnectedGraph(
                navController,
                restaurantViewModel,
                menuViewModel,
                userViewModel,
                cartViewModel,
                orderRepository
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
        "client", "restaurateur" -> Screen.HomeRestaurant.route
        else -> Screen.Welcome.route
    }
}

/////////////////////////////AUTH

fun NavGraphBuilder.addAuthGraph(navController: NavHostController, userViewModel: UserViewModel,
                                 googleSignInClient: GoogleSignInClient, auth: FirebaseAuth) {
    composable(Screen.Welcome.route) {
        WelcomeScreen(navController, userViewModel, googleSignInClient, auth)
    }

    composable(Screen.Login.route) {
        LoginScreen(navController, userViewModel, googleSignInClient, auth)
    }

    composable(Screen.Register.route) {
        RegisterFirstPartScreen(navController, userViewModel, googleSignInClient, auth)
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
            adminUsersViewModel=adminUsersViewModel,
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
        adminRestaurantsViewModel.fetchRestaurants()
        val allRestaurants by adminRestaurantsViewModel.restaurants.observeAsState()
        allRestaurants?.let { theRestaurants ->
            RestaurantPage(
                userViewModel = userViewModel,
                adminRestaurantsViewModel = adminRestaurantsViewModel,
                navController = navController,
                restaurants = theRestaurants
            )
        }
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

    // page pour linker un restaurateur à un restaurant
    composable(Screen.AdminLinkARestorerToAResto.route)
    {
        val selectAdRestaurant by adminRestaurantsViewModel.selected_Restorer.observeAsState()
        selectAdRestaurant?.let { it1 ->
            LinkRestorerRestoAndRestoPage(
                navController,
                adminRestaurantsViewModel,
                adminUsersViewModel,
                it1
            )
        }
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
            orderViewModel = orderViewModel,
            userViewModel = userViewModel
        )
    }

    composable(Screen.DeliveryOrderDetailsPage.route) {
        DeliveryOrderDetailsScreen(
            navController = navController,
            orderViewModel = orderViewModel,
            userViewModel = userViewModel,
            deliveryViewModel = deliveryViewModel
        )
    }

    composable(Screen.DeliveryAllOrdersPage.route) {
        AllOrdersScreen(
            navController = navController,
            deliveryViewModel = deliveryViewModel,
            orderViewModel = orderViewModel,
            userViewModel = userViewModel
        )
    }
}

/////////////////////////////CONNECTED

fun NavGraphBuilder.addConnectedGraph(
    navController: NavHostController,
    restaurantsViewModel: RestaurantViewModel,
    menuViewModel: MenuViewModel,
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel,
    orderRepository: OrderRepository
) {
    composable(Screen.HomeRestaurant.route) {
        HomeScreenRestaurant(navController, restaurantsViewModel, userViewModel, menuViewModel)
    }

    composable(Screen.HomeMenu.route) {
        HomeScreenMenu(navController, restaurantsViewModel, userViewModel, menuViewModel)
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
                FormModifyRestaurantScreen(theRestaurant, restaurantsViewModel, navController)
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
                MenuRestaurantScreen(selectMenu, menuViewModel, cartViewModel, navController)
            } ?: Log.d("ConnectedNavHost", "Menu data is null")
        } else {
            Log.d("ConnectedNavHost", "Invalid role or no menu selected")
        }
    }

    composable(Screen.Cart.route) {
        CartScreen(
            cartViewModel = cartViewModel
        )
    }

    composable(Screen.OrderTracking.route) {
        val user = userViewModel.user.value
        if (user != null) {
            OrderTrackingScreen(
                orderRepository = orderRepository,
                userViewModel = userViewModel
            )
        }
    }

    composable(Screen.Notifications.route) {
        NotificationsScreen(
            userViewModel = userViewModel,
            navController = navController
        )
    }
}


