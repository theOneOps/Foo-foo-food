package com.uds.foufoufood.view.delivery

import android.util.Log
import org.json.JSONObject
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel


@Composable
fun AvailabilityScreen(
    navController: NavHostController,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel
) {

    val isAvailable by deliveryViewModel.isAvailable.collectAsState()

    val newOrder by deliveryViewModel.newOrderAssigned.collectAsState()

    // Utiliser LaunchedEffect pour détecter les nouvelles commandes et naviguer
    LaunchedEffect(newOrder) {
        newOrder?.let {
            // Convertir l'objet JSON en chaîne de caractères ou extraire les données nécessaires
            val orderData = it.toString()
            val orderJson = JSONObject(orderData)
            Log.d("Availability", "New order assigned: $orderData")
            Log.d("Availability", orderData)
            val orderId = orderJson.getJSONObject("order").getString("_id")
            Log.d("Availability", "Order ID: $orderId")
            // Afficher l'ID pour vérification
            Log.d("OrderDetails", "Order ID: $orderId")
            // Naviguer vers l'écran de détail de la commande
            orderViewModel.loadOrder(orderId)
            navController.navigate(Screen.DeliveryOrderPage.route)
            //navController.navigate("orderDetail/$orderData")
        }
    }


    // Utilisation de la Box pour centrer les éléments
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texte pour afficher l'état actuel de disponibilité
            Text(
                text = if (isAvailable) "Disponible pour livrer" else "Indisponible",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isAvailable) Color.Green else Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Switch pour changer l'état de disponibilité
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
