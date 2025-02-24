package com.uds.foufoufood.view.delivery

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.OrderStatus
import com.uds.foufoufood.ui.component.BackButton
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel

val sofiaproBold = FontFamily(Font(R.font.sofiapro_bold))
val sofiaproMedium = FontFamily(Font(R.font.sofiapro_medium))
val sofiaproRegular = FontFamily(Font(R.font.sofiapro_regular))

@Composable
fun DeliveryOrderDetailsScreen(
    navController: NavHostController,
    orderViewModel: OrderViewModel,
    deliveryViewModel: DeliveryViewModel
) {
    val order by orderViewModel.currentOrder.collectAsState()

    BackButton(navController = navController)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        order?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Commande #${it.orderId}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = sofiaproBold,
                        color = colorResource(id = R.color.orange)
                    ),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DetailText(label = "Restaurant", value = it.restaurantName, color = Color.Gray)
                    DetailText(label = "Adresse", value = it.restaurantAddress.toString(), color = colorResource(id = R.color.black))
                    DetailText(label = "Client", value = it.clientName, color = Color.Gray)
                    DetailText(label = "Adresse de livraison", value = it.deliveryAddress.toString(), color = colorResource(id = R.color.black))
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Plats commandés :",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = sofiaproMedium,
                        color = colorResource(id = R.color.orange)
                    )
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    items(it.items) { (menu, quantity) ->
                        MenuItemRow(menu = menu, quantity = quantity)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Statut : ${it.status.displayName}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = sofiaproBold,
                        color = when (it.status) {
                            OrderStatus.WAITING -> Color.Gray
                            OrderStatus.PREPARING -> colorResource(id = R.color.brown)
                            OrderStatus.DELIVERING -> colorResource(id = R.color.orange)
                            OrderStatus.DELIVERED -> Color.Green
                        }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                ActionButtons(
                    order = it,
                    orderViewModel = orderViewModel,
                    navController = navController,
                    deliveryViewModel = deliveryViewModel
                )
            }
        }
    }
}

@Composable
fun DetailText(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                color = colorResource(id = R.color.orange)
            ),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
                color = color
            ),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
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
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = sofiaproMedium
                )
            )
            Text(
                text = menu.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    fontFamily = sofiaproRegular
                )
            )
        }
        Text(
            text = "x$quantity",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = sofiaproMedium
            )
        )
    }
}

@Composable
fun ActionButtons(
    order: Order,
    orderViewModel: OrderViewModel,
    navController: NavHostController,
    deliveryViewModel: DeliveryViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                val nextStatus = when (order.status) {
                    OrderStatus.PREPARING -> OrderStatus.DELIVERING
                    OrderStatus.DELIVERING -> OrderStatus.DELIVERED
                    else -> order.status
                }
                orderViewModel.updateOrderStatus(nextStatus)
                if (nextStatus == OrderStatus.DELIVERED) {
                    deliveryViewModel.clearNewOrder()
                    deliveryViewModel.resetAvailability()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange)),
            enabled = order.status != OrderStatus.DELIVERED && order.status != OrderStatus.WAITING,
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text = "Étape suivante", color = Color.White)
        }

        OutlinedButton(
            onClick = {
                orderViewModel.cancelOrder(navController, deliveryViewModel)
                Log.d("DeliveryOrderDetailsScreen", "Commande annulée")
            },
            enabled = order.status == OrderStatus.PREPARING,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (order.status == OrderStatus.PREPARING) Color.Red else colorResource(id = R.color.grey_stroke), // Rouge si activé, gris si désactivé
                disabledContentColor = Color.Black.copy(alpha = 0.2f),
                disabledContainerColor = Color.Gray.copy(alpha = 0.2f)

            ),
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text = "Annuler")
        }
    }
}
