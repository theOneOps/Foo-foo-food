package com.uds.foufoufood.view

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.navigation.UnifiedNavHost
import com.uds.foufoufood.repository.OrderRepository
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.CartViewModel
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.RestaurantViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun MainScreen(
    userViewModel: UserViewModel,
    navController: NavHostController,
    adminUsersViewModel: AdminUsersViewModel,
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel,
    menuViewModel: MenuViewModel,
    restaurantViewModel: RestaurantViewModel,
    cartViewModel: CartViewModel,
    orderRepository: OrderRepository,
    googleSignInClient: GoogleSignInClient,
    auth: FirebaseAuth
) {
    val context = LocalContext.current
    val user by userViewModel.user.observeAsState()
    val isLoading by userViewModel.loading.observeAsState()
    var emailValidated by remember { mutableStateOf(false) }
    var connectUser by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val token = getToken(context)
        if (token != null) {
            userViewModel.getUserFromToken(token)
        }
    }

    user?.let {
        connectUser = it.role ?: ""
        emailValidated = it.emailValidated ?: false
    }

    if (user == null) {
        connectUser = ""
        emailValidated = false
    }

    val showAdminBottomBar = connectUser == "admin"

    if (isLoading == true) {
        LoadingScreen()
    }

    UnifiedNavHost(
        navController = navController,
        connectUser = connectUser,
        emailValidated = emailValidated,
        userViewModel = userViewModel,
        adminUsersViewModel = adminUsersViewModel,
        adminRestaurantsViewModel = adminRestaurantsViewModel,
        deliveryViewModel = deliveryViewModel,
        orderViewModel = orderViewModel,
        menuViewModel = menuViewModel,
        restaurantViewModel = restaurantViewModel,
        cartViewModel = cartViewModel,
        orderRepository = orderRepository,
        showAdminBottomBar = showAdminBottomBar,
        googleSignInClient = googleSignInClient,
        auth = auth
    )
}