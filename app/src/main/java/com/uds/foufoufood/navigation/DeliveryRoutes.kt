package com.uds.foufoufood.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uds.foufoufood.view.delivery.AvailabilityScreen
import com.uds.foufoufood.viewmodel.DeliveryViewModel

fun NavGraphBuilder.addDeliveryRoutes(
    navController: NavHostController,
    deliveryViewModel: DeliveryViewModel
) {
    composable(Screen.DeliveryAvailablePage.route) {
        AvailabilityScreen (
            navController = navController,
            deliveryViewModel = deliveryViewModel,
        )
    }
}
