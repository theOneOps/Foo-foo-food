package com.uds.foufoufood.ui.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
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
    allMenu: List<Menu>,
    menu: Menu,
    menuViewModel: MenuViewModel
) {
    // State pour contrôler l'affichage du dialog
    val context = LocalContext.current
    val token = getToken(context) ?: ""
    val userId = getUserId(context) ?: ""

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                menuViewModel.setSharedCurrentMenu(menu)
                navController.navigate(Screen.ClientInstanceMenuPage.route)
            }, // Ouvrir le dialog lors du clic
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            // Image en haut
            AsyncImage(
                model = menu.image,
                modifier = Modifier
                    .height(300.dp) // Hauteur de l'image définie
                    .clip(RoundedCornerShape(16.dp)),
                contentDescription = menu.description,
                contentScale = ContentScale.Crop,
            )

            // Texte en dessous de l'image
            Box(
                contentAlignment = Alignment.BottomStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 8.dp
                    ) // Espacement pour que le texte soit plus espacé de l'image
            ) {
                Column(Modifier.padding(top = 2.dp, bottom = 2.dp)) {
                    Text(menu.name, style = TextStyle(Color.Black, fontSize = 30.sp))
                    Text(menu.description, style = TextStyle(Color.Gray, fontSize = 20.sp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    colorResource(R.color.white_grey),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                menu.price.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = colorResource(R.color.grey),
                                fontSize = 12.sp,
                            )
                        }
                        val isConnectedRestaurateur = menuViewModel.isConnectedRestorer.value!!
                        if (isConnectedRestaurateur)
                        {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                TextLink(label = "Delete Menu", onClick = {
                                    // suppression du menu
                                    menuViewModel.deleteMenu(token, menu._id)
                                    Toast.makeText(
                                        context,
                                        "menu bien supprimé",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                })
                            }
                        }
                    }
                }
            }
        }
    }

}