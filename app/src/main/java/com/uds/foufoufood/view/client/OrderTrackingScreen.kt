package com.uds.foufoufood.view.client

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.OrderItem
import com.uds.foufoufood.repository.OrderRepository
import com.uds.foufoufood.viewmodel.OrderTrackingViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import com.uds.foufoufood.viewmodel.factory.OrderTrackingViewModelFactory

@Composable
fun OrderTrackingScreen(
    orderRepository: OrderRepository, userViewModel: UserViewModel
) {
    val orderTrackingViewModel: OrderTrackingViewModel = viewModel(
        factory = OrderTrackingViewModelFactory(orderRepository, userViewModel)
    )

    val order by orderTrackingViewModel.currentOrder.observeAsState()
    val errorMessage by orderTrackingViewModel.errorMessage.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            orderTrackingViewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(Unit) {
        orderTrackingViewModel.fetchCurrentOrder()
    }

    var previousStatus by remember { mutableStateOf(order?.status) }

    LaunchedEffect(order?.status) {
        val currentOrder = order
        if (currentOrder != null && previousStatus != null && currentOrder.status != previousStatus) {
            Toast.makeText(
                context,
                "Statut de la commande mis à jour : ${currentOrder.status.displayName}",
                Toast.LENGTH_SHORT
            ).show()
            previousStatus = currentOrder.status
        } else if (currentOrder == null && previousStatus != null) {
            Toast.makeText(context, "Votre commande a été livrée.", Toast.LENGTH_SHORT).show()
            previousStatus = null
        }
    }

    val currentOrder = order

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (currentOrder != null) {
            Text(
                text = "Détails de la commande",
                style = MaterialTheme.typography.titleLarge.copy(color = colorResource(R.color.orange)),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Restaurant : ${currentOrder.restaurantName}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            val deliveryAddress = currentOrder.deliveryAddress

            if (deliveryAddress != null) {
                Text(
                    text = "Adresse de livraison :",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${deliveryAddress.number} ${deliveryAddress.street}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${deliveryAddress.city}, ${deliveryAddress.state}, ${deliveryAddress.country}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Code postal : ${deliveryAddress.zipCode}",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Adresse de livraison : Non disponible",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Red)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Statut : ${currentOrder.status.displayName}",
                style = MaterialTheme.typography.bodyLarge.copy(color = colorResource(R.color.orange))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Articles :",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentOrder.items) { dish ->
                    OrderItemRow(dish)
                    HorizontalDivider()
                }
            }

            val totalPrice = currentOrder.items.sumOf { it.menu.price * it.quantity }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Prix total : $${"%.2f".format(totalPrice)}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Aucune commande active.", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Button(
            onClick = { orderTrackingViewModel.refreshOrder() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.orange))
        ) {
            Text("Rafraîchir", color = Color.White)
        }
    }
}


@Composable
fun OrderItemRow(dish: OrderItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = dish.menu.name.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Quantité : ${dish.quantity}", style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Prix unitaire : $${"%.2f".format(dish.menu.price)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "$${"%.2f".format(dish.menu.price * dish.quantity)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
