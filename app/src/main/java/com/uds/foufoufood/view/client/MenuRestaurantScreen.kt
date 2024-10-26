package com.uds.foufoufood.view.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.ui.component.CounterProductBought
import com.uds.foufoufood.ui.component.FormModifyMenu
import com.uds.foufoufood.ui.component.HeartIconButton
import com.uds.foufoufood.ui.component.TextLink
import com.uds.foufoufood.viewmodel.MenuViewModel


@Composable
fun MenuRestaurantScreen(menu: Menu, menuViewModel: MenuViewModel)
{
    val context = LocalContext.current
    val token = getToken(context) ?: ""

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column {
                // Box pour l'image en haut
                Box(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = menu.image,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        contentDescription = menu.description,
                        contentScale = ContentScale.Crop,
                    )

                    // Icones sur l'image
                    Box(
                        modifier = Modifier.padding(15.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { /* Action du bouton */ },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .size(50.dp)
                            ) {
                                Text(
                                    "<",
                                    style = TextStyle(fontSize = 30.sp),
                                    color = Color.Gray
                                )
                            }
                            HeartIconButton() // Ton bouton en forme de cœur
                        }
                    }
                }

                // Contenu texte sous l'image
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .background(Color.White),
                ) {
                    Text(menu.name, style = TextStyle(color = Color.Black, fontSize = 35.sp))
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⭐", style = TextStyle(fontSize = 15.sp))
                            Text("${menu.price}", style = TextStyle(fontSize = 18.sp))
                            Spacer(Modifier.width(20.dp))
                            //TextLink(onClick = { /* Lien pour les avis todo */ }, label = "See Review")
                        }
                        // todo : tester si l'user connecté est bien celui qui possède
                        //  le restaurant, voire dans ClientRestaurantScreen

                        val isConnectedRestaurateur = menuViewModel.isConnectedRestorer.value!!
                        if (isConnectedRestaurateur)
                            ModifyMenu(token, menuViewModel, menu)
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("$", style = TextStyle(fontSize = 15.sp))
                            Text("${menu.price}", style = TextStyle(fontSize = 20.sp))
                            Spacer(Modifier.width(20.dp))
                        }

                        // Counter
                        CounterProductBought()
                    }

                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = menu.description.trimIndent()
                    )
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), // Reduced vertical padding
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp)) // Add space as needed
            AddToCartButton()
        }

    }
}

@Composable
fun AddToCartButton() {
    Button(onClick = { /* Ajouter au panier action */ }) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.buy_cart_icon),
                    contentDescription = "Buy Cart Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(8.dp)) // Espacement entre l'icône et le texte
            Text(
                text = "ADD TO CART",
                fontSize = 16.sp,  // Taille du texte
                fontWeight = FontWeight.Bold, // Texte en gras
                color = Color.White // Couleur du texte
            )
        }
    }
}

@Composable
fun ModifyMenu(token:String, menuViewModel: MenuViewModel, menu: Menu) {
    val openDialog = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        TextLink(label = "Modify Menu", onClick = { openDialog.value = true })

        if (openDialog.value) {
            Dialog(onDismissRequest = { openDialog.value = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        FormModifyMenu(menuViewModel,menu, onUpdate = {menuRes->
                            menuViewModel.updateMenu(token,
                                menuRes._id,
                                menuRes.name,
                                menuRes.description,
                                menuRes.price,
                                menuRes.category,
                                menuRes.restaurantId,
                                menuRes.image)
                        })
                    }
                }
            }
        }
    }
}