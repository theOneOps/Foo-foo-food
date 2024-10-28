package com.uds.foufoufood.ui.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.activities.main.TokenManager.getUserId
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.viewmodel.MenuViewModel

@Composable
fun MenuComponent(
    navController: NavController,
    menu: Menu,
    menuViewModel: MenuViewModel
) {
    val context = LocalContext.current
    val token = getToken(context) ?: ""

    Card(
        modifier = Modifier
            .padding(12.dp)
            .clickable {
                menuViewModel.setSharedCurrentMenu(menu)
                navController.navigate(Screen.ClientInstanceMenuPage.route)
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            // Image en haut avec coins arrondis
            AsyncImage(
                model = menu.image,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentDescription = menu.description,
                contentScale = ContentScale.Crop,
            )

            // Contenu du menu
            Column(
                modifier = Modifier
                    .background(colorResource(id = R.color.white))
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = menu.name.replaceFirstChar { it.uppercase() },
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.sofiapro_bold))
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = menu.description,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.sofiapro_medium))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Row for Price and Delete Button (if applicable)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price Tag
                    Box(
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.orange_alpha),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${menu.price} €",
                            color = colorResource(id = R.color.orange),
                            fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
                        )
                    }

                    // Delete Menu Button (if user is restaurateur)
                    val isConnectedRestaurateur = menuViewModel.isConnectedRestorer.value == true
                    if (isConnectedRestaurateur) {
                        TextButton(
                            onClick = {
                                menuViewModel.deleteMenu(token, menu._id)
                                Toast.makeText(
                                    context,
                                    "Menu supprimé avec succès",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Supprimer",
                                textDecoration = TextDecoration.Underline,
                                fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
