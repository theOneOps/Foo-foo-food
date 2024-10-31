package com.uds.foufoufood.view.client

import android.util.Log
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.activities.main.TokenManager.getUserId
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.ui.component.BackButton
import com.uds.foufoufood.ui.component.FormNewMenu
import com.uds.foufoufood.ui.component.MenuComponent
import com.uds.foufoufood.viewmodel.MenuViewModel


@Composable
fun ClientRestaurantScreen(
    navController: NavController, menuViewModel: MenuViewModel, restaurant: Restaurant
) {
    val context = LocalContext.current
    val token = getToken(context) ?: ""
    val userId = getUserId(context) ?: ""

    val scrollState = rememberLazyListState()

    // Observer les menus à partir du ViewModel
    val sortedMenus by menuViewModel.sortedMenus.observeAsState(initial = emptyList())

    val openDialog = remember { mutableStateOf(false) }

    // Lancer la récupération des menus dans un effet à composition stable
    LaunchedEffect(Unit) {
        menuViewModel.getAllMenusByRestaurant(token, restaurant._id)
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
                    // Le texte dans le dialog
                    FormNewMenu(navController, restaurant, menuViewModel)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
            item(key = "banner") {
                RestaurantBanner(restaurant, navController)
            }
            item(key = "filterMenus") {
                FilterMenus(menuViewModel) { sortOrder ->
                    menuViewModel.updateSortOrder(sortOrder)
                }
            }

            val isConnectedRestaurateur = userId == restaurant.userId
            menuViewModel.setIsConnectedRestorer(isConnectedRestaurateur)

            item(key = "menus") {
                PrintAllMenus(navController, sortedMenus, menuViewModel)
            }
        }

        // Bouton flottant en bas à droite
        if (userId == restaurant.userId) {
            FloatingActionButton(
                onClick = { openDialog.value = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape),
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.orange_pale),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter un menu",
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun RestaurantBanner(restaurant: Restaurant, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Bouton de retour en haut à gauche
        BackButton(navController = navController)

        // Conteneur principal pour l'image et les informations du restaurant
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        ) {
            // Image du restaurant avec la spécialité en bas à droite
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                // Image du restaurant
                AsyncImage(
                    model = restaurant.imageUrl,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.2f)),
                    contentDescription = restaurant.name,
                    contentScale = ContentScale.Crop
                )

                // Box de spécialité en bas à droite de l'image
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .offset(
                            x = (4).dp, y = (-4).dp
                        ) // Décalage pour être bien positionné sur le bord
                ) {
                    Text(
                        text = restaurant.speciality,
                        fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.orange),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nom du restaurant
            Text(
                text = restaurant.name, style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.sofiapro_bold))
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Adresse du restaurant
            Text(
                text = "${restaurant.address}", style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.sofiapro_medium))
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun PrintAllMenus(
    navController: NavController,
    menus: List<Menu>,
    menuViewModel: MenuViewModel,
) {
    menus.forEach { e ->
        MenuComponent(navController, e, menuViewModel)
    }
}

@Composable
fun FilterMenus(menuViewModel: MenuViewModel, onSortChanged: (sortOrder: Int) -> Unit) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = menuViewModel.sortOrder.observeAsState(0).value ?: 0

    // Options de tri
    val sortOptions = listOf("Prix croissant", "Prix décroissant")

    // Appeler onSortChanged chaque fois que l'option de tri change
    LaunchedEffect(itemPosition) {
        Log.d("FilterMenus", "Sort option selected: $itemPosition")
        onSortChanged(itemPosition)
    }

    Row(
        modifier = Modifier
            .padding(start = 20.dp, bottom = 15.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Trier par : ",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.sofiapro_medium))
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Row(modifier = Modifier
                    .clickable { isDropDownExpanded.value = true }
                    .padding(8.dp)
                    .background(
                        color = colorResource(id = R.color.orange_alpha),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = sortOptions[itemPosition], style = TextStyle(
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.orange),
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.expand_arrow),
                        contentDescription = "Dropdown Icon",
                        tint = colorResource(id = R.color.orange),
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = isDropDownExpanded.value,
                    onDismissRequest = { isDropDownExpanded.value = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    sortOptions.forEachIndexed { index, option ->
                        DropdownMenuItem(text = {
                            Text(
                                text = option,
                                fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(id = R.color.orange)
                            )
                        }, onClick = {
                            isDropDownExpanded.value = false
                            menuViewModel.updateSortOrder(index)
                        })
                    }
                }
            }
        }
    }
}