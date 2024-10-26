package com.uds.foufoufood.view.delivery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.DrawerScaffold
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun DeliveryOrderScreen(
    navController: NavHostController,
    orderViewModel: OrderViewModel,
    userViewModel: UserViewModel // Pass userViewModel for logout
) {
    // Observe the current order from the ViewModel
    val order by orderViewModel.currentOrder.collectAsState()

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

            // Display the main content of the screen
            order?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Order Number
                    Text(
                        text = "Commande #${it.orderId}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Restaurant Information
                    Text(
                        text = "Restaurant : ${it.restaurantName}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Adresse du restaurant: ${it.restaurantAddress}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Client Information
                    Text(
                        text = "Client à livrer : ${it.clientName}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Adresse de livraison : ${it.deliveryAddress}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ordered Items
                    Text(
                        text = "Plats commandés :",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(it.items) { (menu, quantity) ->
                            MenuItemRow(menu = menu, quantity = quantity)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Order Status
                    Text(
                        text = "Statut : ${it.status.displayName}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = when (it.status) {
                            OrderStatus.WAITING -> Color.Gray
                            OrderStatus.PREPARING -> Color.Blue
                            OrderStatus.DELIVERING -> Color.Magenta
                            OrderStatus.DELIVERED -> Color.Green
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val nextStatus = when (it.status) {
                                    OrderStatus.PREPARING -> OrderStatus.DELIVERING
                                    OrderStatus.DELIVERING -> OrderStatus.DELIVERED
                                    else -> it.status
                                }
                                orderViewModel.updateOrderStatus(nextStatus)
                            },
                            enabled = it.status != OrderStatus.DELIVERED && it.status != OrderStatus.WAITING
                        ) {
                            Text(text = "Étape suivante")
                        }

                        OutlinedButton(
                            onClick = { orderViewModel.cancelOrder() },
                            enabled = it.status != OrderStatus.WAITING
                        ) {
                            Text(text = "Annuler la commande", color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MenuItemRow(menu: Menu, quantity: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = menu.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = menu.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Text(
            text = "x$quantity",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
