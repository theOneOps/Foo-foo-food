package com.uds.foufoufood.view.delivery

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.DrawerScaffold
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import org.json.JSONObject


@Composable
fun AvailabilityScreen(
    navController: NavHostController,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel,
    userViewModel: UserViewModel
) {
    val isAvailable by deliveryViewModel.isAvailable.collectAsState()
    val newOrder by deliveryViewModel.newOrderAssigned.collectAsState()
    val currentOrder by orderViewModel.currentOrder.collectAsState()

    // Check for an ongoing order when the screen loads
    val email = deliveryViewModel.currentDeliveryManEmail

    LaunchedEffect(email) {
        if (!email.isNullOrEmpty()) {
            orderViewModel.loadOrderByDeliverManEmail(email)
        }
    }

    // Navigate to the order details page if there’s a current order
    LaunchedEffect(currentOrder) {
        currentOrder?.let {
            navController.navigate(Screen.DeliveryOrderDetailsPage.route)
        }
    }

    // Detect new orders and navigate to the order details page
    LaunchedEffect(newOrder) {
        newOrder?.let {
            val orderData = it.toString()
            val orderJson = JSONObject(orderData)
            val orderId = orderJson.getJSONObject("order").getString("_id")

            Log.d("Availability", "New order assigned: $orderData")
            Log.d("OrderDetails", "Order ID: $orderId")

            orderViewModel.loadOrder(orderId)
            navController.navigate(Screen.DeliveryOrderDetailsPage.route)
        }
    }

    DrawerScaffold(
        navController = navController,
        userViewModel = userViewModel,
        currentScreen = Screen.DeliveryOrderDetailsPage.route
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp, top = 20.dp)
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Screen.DeliveryOrderDetailsPage.route)
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isAvailable) "Disponible pour livrer" else "Indisponible",
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (isAvailable) Color.Green else Color.Red
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { deliveryViewModel.setAvailability(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            uncheckedThumbColor = Color.Red
                        )
                    )
                }
            }
        }
    }
}

