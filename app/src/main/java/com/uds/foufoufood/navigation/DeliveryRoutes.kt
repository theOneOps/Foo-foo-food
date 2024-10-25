//package com.uds.foufoufood.navigation
//
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.composable
//import com.uds.foufoufood.view.delivery.AvailabilityScreen
//import com.uds.foufoufood.view.delivery.DeliveryOrderScreen
//import com.uds.foufoufood.viewmodel.DeliveryViewModel
//import com.uds.foufoufood.viewmodel.OrderViewModel
//
//fun NavGraphBuilder.addDeliveryRoutes(
//    navController: NavHostController,
//    deliveryViewModel: DeliveryViewModel,
//    orderViewModel: OrderViewModel
//) {
//    composable(Screen.DeliveryAvailablePage.route) {
//        AvailabilityScreen (
//            navController = navController,
//            deliveryViewModel = deliveryViewModel,
//            orderViewModel = orderViewModel
//        )
//    }
//
//    composable(Screen.DeliveryOrderPage.route) {
//        DeliveryOrderScreen (
//            navController = navController,
//            orderViewModel = orderViewModel
//        )
//    }
//}
