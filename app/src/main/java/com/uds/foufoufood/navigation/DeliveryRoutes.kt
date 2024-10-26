package com.uds.foufoufood.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uds.foufoufood.view.delivery.DeliveryOrderScreen
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

fun NavGraphBuilder.addDeliveryRoutes(
    navController: NavHostController,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel,
    userViewModel: UserViewModel
) {
//    composable(Screen.DeliveryAvailablePage.route) {
//        AvailabilityScreen (
//            navController = navController,
//            deliveryViewModel = deliveryViewModel,
//            orderViewModel = orderViewModel
//        )
//    }

    composable(Screen.DeliveryOrderPage.route) {
        DeliveryOrderScreen (
            navController = navController,
            orderViewModel = orderViewModel,
            userViewModel=userViewModel
        )
    }
}
