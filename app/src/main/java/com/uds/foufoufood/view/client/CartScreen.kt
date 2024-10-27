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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.CartItem
import com.uds.foufoufood.viewmodel.CartViewModel

@Composable
fun CartScreen(cartViewModel: CartViewModel) {
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val errorMessage by cartViewModel.errorMessage.observeAsState()
    val orderSuccessMessage by cartViewModel.orderSuccessMessage.observeAsState()
    val context = LocalContext.current

    // Display error and success messages as Toasts
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            cartViewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(orderSuccessMessage) {
        orderSuccessMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            cartViewModel.clearSuccessMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Votre Panier",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cartItems) { item ->
                CartItemRow(
                    item = item,
                    onRemove = { cartViewModel.removeItem(item) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CheckoutButton(
            totalPrice = cartItems.sumOf { it.menu.price * it.quantity },
            onClick = { cartViewModel.checkout() },
            enabled = cartItems.isNotEmpty()
        )
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.menu.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Quantité: ${item.quantity}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "$${"%.2f".format(item.menu.price * item.quantity)}",
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = onRemove) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Retirer un",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun CheckoutButton(totalPrice: Double, onClick: () -> Unit, enabled: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "Total: $${"%.2f".format(totalPrice)}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.orange)),
            modifier = Modifier
                .fillMaxWidth(0.8f) // Centered and not too wide
        ) {
            Text("Passer la commande")
        }
    }
}






