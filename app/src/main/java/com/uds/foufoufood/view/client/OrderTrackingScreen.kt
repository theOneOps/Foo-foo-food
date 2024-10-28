package com.uds.foufoufood.view.client

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.OrderItem
import com.uds.foufoufood.viewmodel.OrderTrackingViewModel

@Composable
fun OrderTrackingScreen(
    orderTrackingViewModel: OrderTrackingViewModel) {
    val order by orderTrackingViewModel.currentOrder.observeAsState()
    val errorMessage by orderTrackingViewModel.errorMessage.observeAsState()
    val context = LocalContext.current

    // Afficher les messages d'erreur
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            orderTrackingViewModel.clearErrorMessage()
        }
    }

    // Récupérer la commande actuelle lorsque l'écran est chargé
    LaunchedEffect(Unit) {
        orderTrackingViewModel.fetchCurrentOrder()
    }

    // Statut précédent pour comparaison
    var previousStatus by remember { mutableStateOf(order?.status) }

    // Notifier lorsque le statut de la commande change
    LaunchedEffect(order?.status) {
        if (order != null && previousStatus != null && order!!.status != previousStatus) {
            Toast.makeText(context, "Statut de la commande mis à jour : ${order!!.status.displayName}", Toast.LENGTH_SHORT).show()
            previousStatus = order!!.status
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (order != null) {
            // Afficher les détails de la commande
            Text(
                text = "Détails de la commande",
                style = MaterialTheme.typography.titleLarge.copy(color = colorResource(R.color.orange)),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Informations sur le restaurant
            Text(
                text = "Restaurant : ${order!!.restaurantName ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Adresse de livraison
            val deliveryAddress = order!!.deliveryAddress
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

            // Statut de la commande
            Text(
                text = "Statut : ${order!!.status.displayName}",
                style = MaterialTheme.typography.bodyLarge.copy(color = colorResource(R.color.orange))
            )

//            if (!order!!.deliveryManEmail.isNullOrEmpty()) {
//                Text(
//                    text = "Livreur : ${order!!.deliveryManEmail}",
//                    style = MaterialTheme.typography.bodyLarge
//                )
//            }

            Spacer(modifier = Modifier.height(16.dp))

            // Articles de la commande
            Text(
                text = "Articles :",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Liste des articles avec poids pour prendre plus d'espace
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Permet à la liste de prendre l'espace disponible
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(order!!.items) { dish ->
                    OrderItemRow(dish)
                    Divider()
                }
            }

            // Prix total
            val totalPrice = order!!.items.sumOf { it.menu.price * it.quantity }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Prix total : $${"%.2f".format(totalPrice)}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))
        } else {
            // Afficher un message s'il n'y a pas de commande active
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Prend l'espace disponible
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Aucune commande active.", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // Inclure le bouton de rafraîchissement en bas sans espace inutile
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
        // Détails de l'article
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = dish.menu.name.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Quantité : ${dish.quantity}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Prix unitaire : $${"%.2f".format(dish.menu.price)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        // Sous-total
        Text(
            text = "$${"%.2f".format(dish.menu.price * dish.quantity)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
