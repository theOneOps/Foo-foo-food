package com.uds.foufoufood.view.delivery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.DrawerScaffold
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun AllOrdersScreen(
    navController: NavHostController,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel,
    userViewModel: UserViewModel
) {
    val deliveryOrders by orderViewModel.orders.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.user.value?.email?.let { email ->
            orderViewModel.fetchOrdersForDeliveryMan(email)
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
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                Text(
                    text = "Historique des commandes",
                    fontSize = 26.sp,
                    color = colorResource(R.color.brown),
                    fontFamily = FontFamily(Font(R.font.sofiapro_bold)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 70.dp)
                ) {
                    items(deliveryOrders) { order ->
                        OrderItemRow(order, navController, orderViewModel)
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    deliveryViewModel.refreshAvailability()
                    navController.navigate(Screen.DeliveryAvailablePage.route)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .shadow(6.dp, shape = MaterialTheme.shapes.medium),
                containerColor = Color.White,
                contentColor = colorResource(R.color.orange),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Nouvelle livraison",
                        tint = colorResource(R.color.orange)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nouvelle livraison",
                        color = colorResource(R.color.orange),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItemRow(
    order: Order, navController: NavHostController, orderViewModel: OrderViewModel
) {
    val isDelivered = order.status == OrderStatus.DELIVERED
    val backgroundColor =
        if (isDelivered) colorResource(R.color.grey_bg_deactivated) else Color.White
    val borderColor = if (isDelivered) Color.Gray else colorResource(R.color.orange)
    val iconTintColor =
        if (isDelivered) colorResource(R.color.grey_bg_deactivated) else colorResource(R.color.orange)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, shape = MaterialTheme.shapes.medium)
            .shadow(2.dp, shape = MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Commande #${order.orderId}",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                color = colorResource(R.color.orange_pale)
            )
            Spacer(modifier = Modifier.height(15.dp))
            val displayStatus = order.status.displayName
            Text(
                text = "Statut : $displayStatus",
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
            )
        }

        IconButton(
            onClick = {
                orderViewModel.setCurrentOrder(order)
                navController.navigate(Screen.DeliveryOrderDetailsPage.route)
            }, enabled = !isDelivered
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Voir détails",
                tint = iconTintColor
            )
        }
    }
}
