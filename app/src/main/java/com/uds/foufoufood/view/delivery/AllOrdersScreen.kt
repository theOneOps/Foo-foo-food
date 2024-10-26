package com.uds.foufoufood.view.delivery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.view.DrawerContent
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun AllOrdersScreen(
    navController: NavHostController,
    orderViewModel: OrderViewModel,
    userViewModel: UserViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Récupérer la liste d'ordres depuis le ViewModel
    val deliveryOrders by orderViewModel.orders.collectAsState()

    LaunchedEffect(Unit) {
        // Charger les commandes de l'utilisateur livreur
        userViewModel.user.value?.email?.let { email ->
            orderViewModel.fetchOrdersForDeliveryMan(email)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                closeDrawer = { scope.launch { drawerState.close() } },
                logout = userViewModel::logout,
                userViewModel = userViewModel,
                currentScreen = Screen.DeliveryOrderDetailsPage.route
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 20.dp, top = 20.dp)
            ) {
                // Bouton pour ouvrir le drawer
                IconButton(
                    onClick = {
                        scope.launch { drawerState.open() }
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }

                // Afficher la liste des commandes
                Column(
                    modifier = Modifier.fillMaxSize().padding(top = 80.dp)
                ) {
                    Text(
                        text = "Historique des commandes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        // Utilisation correcte de `items` avec la liste des commandes
                        items(deliveryOrders) { order ->
                            OrderItemRow(order, navController, orderViewModel)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun OrderItemRow(
    order: Order,
    navController: NavHostController,
    orderViewModel: OrderViewModel
) {
    // Détermine si l'élément doit être désactivé en fonction de son statut
    val isDelivered = order.status == OrderStatus.DELIVERED
    val backgroundColor = if (isDelivered) Color.Gray else Color.LightGray
    val iconTintColor = if (isDelivered) Color.DarkGray else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Informations sur la commande
        Column {
            Text(
                text = "Commande #${order.orderId}",
                fontWeight = FontWeight.Bold,
                color = if (isDelivered) Color.DarkGray else Color.Black
            )
            Text(text = "Statut : ${order.status.displayName}", color = Color.DarkGray)
        }

        // Bouton pour voir plus de détails
        IconButton(
            onClick = {
                // Charger la commande et naviguer vers la page de détail
                orderViewModel.setCurrentOrder(order)
                navController.navigate(Screen.DeliveryOrderDetailsPage.route)
            },
            enabled = !isDelivered // Désactive le bouton visuellement si livrée, mais toujours clickable
        ) {
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "Voir détails",
                tint = iconTintColor
            )
        }
    }
}

