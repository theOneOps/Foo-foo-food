package com.uds.foufoufood.view.delivery

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import kotlinx.coroutines.delay
import org.json.JSONObject

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun AvailabilityScreen(
    navController: NavHostController,
    deliveryViewModel: DeliveryViewModel,
    orderViewModel: OrderViewModel
) {
    val isAvailable by deliveryViewModel.isAvailable.collectAsState()
    val newOrder by deliveryViewModel.newOrderAssigned.collectAsState()
    var showTick by remember { mutableStateOf(false) }

    // Vérifiez s'il y a déjà une commande en cours à l'arrivée sur l'écran
    val email = deliveryViewModel.currentDeliveryManEmail

    // Animation de l'affichage "En attente d'une commande..."
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = ""
    )

    LaunchedEffect(email) {
        if (!email.isNullOrEmpty()) {
            orderViewModel.loadOrderByDeliverManEmail(email)
        }
    }

    // Detect new orders and navigate to the order details page
    LaunchedEffect(newOrder) {
        newOrder?.let {
            // Convertir l'objet JSON en chaîne de caractères ou extraire les données nécessaires
            val orderData = it.toString()
            val orderJson = JSONObject(orderData)
            val orderId = orderJson.getJSONObject("order").getString("_id")

            Log.d("Availability", "New order assigned: $orderData")
            Log.d("OrderDetails", "Order ID: $orderId")

            orderViewModel.loadOrder(orderId)
            showTick = true
            delay(2000L)
            navController.navigate(Screen.DeliveryOrderDetailsPage.route)
            showTick = false
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        IconButton(
            onClick = { navController.navigate(Screen.DeliveryAllOrdersPage.route) }, // Action de retour
            modifier = Modifier
                .shadow(8.dp, shape = RoundedCornerShape(12.dp))
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .align(Alignment.TopStart)
                .size(42.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Retour",
                tint = colorResource(id = R.color.orange),
                modifier = Modifier.size(20.dp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.logo_only),
            contentDescription = "Logo",
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.TopEnd)
        )

        // Titre principal au centre en haut
        Text(
            text = "Statut de Livraison",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.sofiapro_bold)),
            color = colorResource(id = R.color.orange),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp) // Ajustement en fonction de l'espace entre le logo et le titre
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            if (showTick) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Nouvelle commande assignée",
                    tint = colorResource(id = R.color.orange),
                    modifier = Modifier.size(100.dp)
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Texte de message dynamique
                    Text(
                        text = if (isAvailable) "En attente d'une commande..." else "Appuyez pour prendre une commande",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                        color = colorResource(id = R.color.orange),
                        modifier = Modifier.scale(if (isAvailable) scale else 1f)

                    )

                    // Switch de disponibilité
                    Box(modifier = Modifier.scale(1.3f)) {
                        Switch(
                            checked = isAvailable,
                            onCheckedChange = { deliveryViewModel.setAvailability(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colorResource(id = R.color.white),
                                uncheckedThumbColor = Color.Gray,
                                checkedTrackColor = colorResource(id = R.color.orange)
                            )
                        )
                    }

                    // Texte de disponibilité
                    Text(
                        text = if (isAvailable) "Status Livraison : Disponible pour livrer" else "Status Livraison : Indisponible",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                        color = if (isAvailable) colorResource(id = R.color.green) else Color.Gray
                    )
                }
            }
        }
    }
    // Logo en bas de page avec animation de montée
    val logoOffset by animateDpAsState(targetValue = if (isAvailable) 380.dp else 1000.dp,
        label = ""
    )

    // Logo centré en bas avec animation
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.offset(y = logoOffset) // Applique l'animation d'offset vertical
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_only),
                contentDescription = "Logo",
                modifier = Modifier.size(1000.dp)
            )
        }
    }
}