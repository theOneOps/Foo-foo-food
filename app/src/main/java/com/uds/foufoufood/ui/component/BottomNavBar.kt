package com.uds.foufoufood.ui.component

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.viewmodel.NotificationViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import com.uds.foufoufood.viewmodel.factory.NotificationViewModelFactory

@Composable
fun BottomNavBarClient(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val activity = LocalContext.current as ComponentActivity
    val notificationViewModel: NotificationViewModel = viewModel(
        activity,
        factory = NotificationViewModelFactory(userViewModel)
    )
    val notifications by notificationViewModel.notifications.observeAsState(emptyList())
    val unreadCount = notifications.count { !it.isRead }

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier.height(50.dp)
    ) {
        // Cart Item
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart"
                )
            },
            selected = selectedItem == 1,
            onClick = {
                onItemSelected(1)
                navController.navigate(Screen.Cart.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        // Notifications Item with Badge
        NavigationBarItem(
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge {
                                Text(unreadCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            },
            selected = selectedItem == 2,
            onClick = {
                onItemSelected(2)
                navController.navigate(Screen.Notifications.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}


