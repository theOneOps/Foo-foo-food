package com.uds.foufoufood.view.client

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.data_class.model.CartItem
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.ui.component.BackButton
import com.uds.foufoufood.ui.component.CounterProductBought
import com.uds.foufoufood.ui.component.FormModifyMenu
import com.uds.foufoufood.viewmodel.CartViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel


@Composable
fun MenuRestaurantScreen(
    menu: Menu,
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val token = getToken(context) ?: ""
    val quantity = remember { mutableIntStateOf(1) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        BackButton(navController = navController)

        LazyColumn(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    AsyncImage(
                        model = menu.image,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.2f)),
                        contentDescription = menu.name,
                        contentScale = ContentScale.Crop
                    )

                    val isConnectedRestaurateur = menuViewModel.isConnectedRestorer.value!!
                    if (isConnectedRestaurateur) Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .offset(
                                x = (-4).dp, y = (-4).dp
                            ),
                    ) {
                        ModifyMenu(token, menuViewModel, menu, navController)
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .offset(
                                x = (4).dp, y = (-4).dp
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    color = colorResource(id = R.color.orange),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = menu.category.replaceFirstChar { it.uppercase() },
                                fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                                color = colorResource(id = R.color.white),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 2.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Text(
                    text = menu.name.replaceFirstChar { it.uppercase() }, style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black),
                        fontFamily = FontFamily(Font(R.font.sofiapro_bold))
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Text(
                    text = menu.description, style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.sofiapro_medium))
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Text(
                            text = menu.price.toString(), style = TextStyle(
                                fontSize = 35.sp,
                                color = colorResource(id = R.color.orange),
                                fontFamily = FontFamily(Font(R.font.sofiapro_medium))
                            ), modifier = Modifier.padding(start = 4.dp)
                        )

                        Text(
                            text = "$", style = TextStyle(
                                fontSize = 15.sp,
                                color = colorResource(id = R.color.orange),
                                fontFamily = FontFamily(Font(R.font.sofiapro_medium))
                            ), modifier = Modifier.padding(start = 5.dp)
                        )
                    }

                    CounterProductBought(quantity)
                }
            }

            item {
                for (ingredient in menu.ingredients) {
                    Text(
                        text = "- ${ingredient.replaceFirstChar { it.uppercase() }}",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.sofiapro_medium))
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, start = 10.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(1.dp)
                            .background(color = Color.Gray.copy(alpha = 0.3f))
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AddToCartButton(menu, cartViewModel, quantity.intValue)
                }
            }
        }
    }
}

@Composable
fun AddToCartButton(
    menu: Menu, cartViewModel: CartViewModel, quantity: Int
) {
    val context = LocalContext.current
    val scale = remember { mutableFloatStateOf(1f) }
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = {
            val restaurantId = menu.restaurantId
            val cartItems = cartViewModel.cartItems.value ?: emptyList()
            val isCartEmptyOrSameRestaurant =
                cartItems.isEmpty() || cartItems.all { it.menu.restaurantId == restaurantId }

            if (isCartEmptyOrSameRestaurant) {
                if (quantity <= 0) {
                    Toast.makeText(
                        context,
                        "Veuillez entrer une quantité valide",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    cartViewModel.addItem(
                        CartItem(
                            menu = menu,
                            quantity = quantity,
                        ), restaurantId = restaurantId
                    )
                    Toast.makeText(context, "${menu.name} ajouté au panier", Toast.LENGTH_SHORT)
                        .show()

                }
            } else {
                showDialog = true
            }

            scale.floatValue = 0.9f
        },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.orange)),
        modifier = Modifier
            .scale(scale.floatValue)
            .padding(8.dp)
    ) {
        LaunchedEffect(scale.floatValue) {
            if (scale.floatValue == 0.9f) {
                scale.floatValue = 1f
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(3.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.buy_cart_icon),
                    contentDescription = "Icône d'achat",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Ajouter au panier",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.sofiapro_bold))
            )
        }
    }

    if (showDialog) {
        androidx.compose.material3.AlertDialog(onDismissRequest = { showDialog = false },
            title = { Text("Vider le panier ?") },
            text = { Text("Votre panier contient des articles d'un autre restaurant. Vider le panier et ajouter cet article ?") },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    cartViewModel.clearCart()
                    cartViewModel.addItem(
                        CartItem(
                            menu = menu, quantity = quantity
                        ), restaurantId = menu.restaurantId
                    )
                    Toast.makeText(context, "${menu.name} ajouté au panier", Toast.LENGTH_SHORT)
                        .show()
                    showDialog = false
                }) {
                    Text("Oui")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDialog = false }) {
                    Text("Non")
                }
            })
    }
}

@Composable
fun ModifyMenu(
    token: String, menuViewModel: MenuViewModel, menu: Menu, navController: NavController
) {
    val openDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(
                color = colorResource(id = R.color.white), shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 6.dp), verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Modifier",
            fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
            color = colorResource(id = R.color.orange),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = {
                    openDialog.value = true
                })
                .padding(top = 2.dp)
        )
    }

    if (openDialog.value) {
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 700.dp)
                    .clip(RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    FormModifyMenu(menu, onUpdate = { menuRes ->
                        menuViewModel.updateMenu(
                            token,
                            menuRes._id,
                            menuRes.name,
                            menuRes.description,
                            menuRes.price,
                            menuRes.category,
                            menuRes.restaurantId,
                            menuRes.image,
                            menuRes.ingredients
                        )
                        openDialog.value = false
                    }, navController)
                }
            }
        }
    }
}