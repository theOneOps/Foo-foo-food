package com.uds.foufoufood.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel


@Composable
fun DeliveryNavHost(
    navController: NavHostController,
    deliveryViewModel : DeliveryViewModel,
    orderViewModel: OrderViewModel
) {
    Scaffold(
        topBar = { },
        bottomBar = { },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.DeliveryAvailablePage.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            addDeliveryRoutes(navController, deliveryViewModel, orderViewModel)
        }
    }
}