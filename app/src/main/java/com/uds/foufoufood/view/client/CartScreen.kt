package com.uds.foufoufood.view.client

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.CartItem
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.DrawerScaffold
import com.uds.foufoufood.ui.component.TitlePage
import com.uds.foufoufood.ui.component.ValidateButton
import com.uds.foufoufood.viewmodel.CartViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel, navController: NavHostController, userViewModel: UserViewModel
) {
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val errorMessage by cartViewModel.errorMessage.observeAsState()
    val orderSuccessMessage by cartViewModel.orderSuccessMessage.observeAsState()
    val context = LocalContext.current

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

    DrawerScaffold(
        navController = navController,
        userViewModel = userViewModel,
        currentScreen = Screen.Profile.route
    ) {

        Column(
            modifier = Modifier
                .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            TitlePage(label = "Mon panier")

            LazyColumn(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems) { item ->
                    CartItemRow(item = item,
                        onIncrement = { cartViewModel.incrementQuantity(item) },
                        onDecrement = { cartViewModel.decrementQuantity(item) },
                        onDelete = { cartViewModel.removeItem(item) })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CheckoutButton(totalPrice = cartItems.sumOf { it.menu.price * it.quantity },
                onClick = { cartViewModel.checkout() }
            )
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem, onIncrement: () -> Unit, onDecrement: () -> Unit, onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.menu.image,
            contentDescription = item.menu.name,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(15.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.menu.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.sofiapro_medium))
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Prix total : $${"%.2f".format(item.menu.price * item.quantity)}",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
                fontSize = 15.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrement) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Diminuer la quantité",
                        tint = colorResource(id = R.color.orange)
                    )
                }
                Text(
                    text = item.quantity.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = onIncrement) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Augmenter la quantité",
                        tint = colorResource(id = R.color.orange)
                    )
                }
            }
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Supprimer l'article",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun CheckoutButton(totalPrice: Double, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = "Total: $${"%.2f".format(totalPrice)}",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        ValidateButton(
            label = "Valider la commande", onClick = onClick
        )
    }
}