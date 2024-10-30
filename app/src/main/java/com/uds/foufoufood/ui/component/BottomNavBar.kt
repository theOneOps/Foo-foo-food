package com.uds.foufoufood.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.viewmodel.NotificationViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import com.uds.foufoufood.viewmodel.factory.NotificationViewModelFactory

@Composable
fun BottomNavBarClient(
    selectedItem: Int,
    onclick: (Int) -> Unit,
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
        containerColor = colorResource(id = R.color.white),
        contentColor = colorResource(id = R.color.white),
    ) {
        // Liste des éléments avec des icônes Material et des libellés
        val items = listOf(
            Pair("Restaurants", R.drawable.storefront),
            Pair("Menus", R.drawable.menubook),
            Pair("Panier", R.drawable.cart),
            Pair("Notifications", R.drawable.notifications)
        )

        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index

            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = item.second),
                        contentDescription = item.first,
                        modifier = Modifier.size(if (isSelected) 28.dp else 24.dp),
                        tint = if (isSelected) colorResource(id = R.color.orange) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                selected = isSelected,
                onClick = { onclick(index) },
                label = {
                    Text(
                        text = item.first,
                        fontSize = 12.sp,
                        color = if (isSelected) colorResource(id = R.color.orange) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(id = R.color.orange),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.0f),
                    selectedTextColor = colorResource(id = R.color.orange),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
            )
        }
    }
}

