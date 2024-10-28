package com.uds.foufoufood.view.client

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.uds.foufoufood.data_class.model.Notification
import com.uds.foufoufood.viewmodel.NotificationViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import com.uds.foufoufood.viewmodel.factory.NotificationViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    userViewModel: UserViewModel,
    navController: NavController
) {
    val activity = LocalContext.current as ComponentActivity
    val notificationViewModel: NotificationViewModel = viewModel(
        activity,
        factory = NotificationViewModelFactory(userViewModel)
    )

    val notifications by notificationViewModel.notifications.observeAsState(emptyList())
    val errorMessage by notificationViewModel.errorMessage.observeAsState()

    val context = LocalContext.current

    // Add this LaunchedEffect to mark all notifications as read when the screen is opened
    LaunchedEffect(Unit) {
        notificationViewModel.markAllNotificationsAsRead()
    }

    // Display error messages
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            notificationViewModel.clearErrorMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Notifications") })
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(notifications) { notification ->
                NotificationItem(
                    notification = notification,
                    onClick = {
                        // Handle notification click
                        notificationViewModel.markNotificationAsRead(notification.id)
                        // Navigate to order details or appropriate screen
                        navController.navigate("orderTracking")
                    }
                )
                Divider()
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (notification.isRead) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary.copy(
            alpha = 0.1f
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = formatTimestamp(notification.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        if (!notification.isRead) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = "Unread",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
    val date = java.util.Date(timestamp)
    return sdf.format(date)
}