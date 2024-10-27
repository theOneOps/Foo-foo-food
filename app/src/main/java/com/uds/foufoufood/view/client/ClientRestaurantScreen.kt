package com.uds.foufoufood.view.client

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
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
import com.uds.foufoufood.ui.component.FormNewMenu
import com.uds.foufoufood.ui.component.MenuComponent
import com.uds.foufoufood.ui.component.TextLink
import com.uds.foufoufood.viewmodel.MenuViewModel


@Composable
fun ClientRestaurantScreen(
    navController: NavController,
    menuViewModel: MenuViewModel,
    restaurant: Restaurant
) {

    // Obtenir le contexte
    val context = LocalContext.current
    val token = getToken(context) ?: ""
    val userId = getUserId(context) ?: ""

    // Observer les menus à partir du ViewModel
    val menus by menuViewModel.menus.observeAsState(initial = emptyList())

    // Lancer la récupération des menus dans un effet à composition stable
    LaunchedEffect(Unit) {
        menuViewModel.getAllMenusByRestaurant(token, restaurant._id)
    }

    LazyColumn {
        item {
            RestaurantComponent(restaurant)
        }
        item {
            //println("userId vaut : $userId")
            if (userId == restaurant.userId)
                AddNewMenu(restaurant, menuViewModel)
        }
        item {
            FilterMenus()
        }

        println(menus)

        val isConnectedRestaurateur = userId == restaurant.userId
        menuViewModel.setIsConnectedRestorer(isConnectedRestaurateur)
        item {
            menus?.let { PrintAllMenus(navController,it, menuViewModel) }
        }
    }
}


@Composable
fun AddNewMenu(restaurant: Restaurant, menuViewModel: MenuViewModel) {
    // État pour contrôler l'affichage du dialog
    val openDialog = remember { mutableStateOf(false) }

    // Le Box avec TextLink
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        TextLink(label = "Add Menu", onClick = {
            openDialog.value = true // Ouvrir le dialog lors du clic
        })
    }

    // Si openDialog est true, afficher le dialog
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
                    // Le texte dans le dialog
                    FormNewMenu(restaurant, menuViewModel)
                }
            }
        }
    }
}


// test restaurants avec des menu
@Composable
fun RestaurantComponent(restaurant: Restaurant) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        // Image à droite
        Box(
            modifier = Modifier
                .graphicsLayer {
                    this.transformOrigin = TransformOrigin(1f, 0.5f)
                    this.rotationX = 4f
                }
                .fillMaxWidth(0.75f)
                .align(Alignment.CenterEnd)
                .offset(x = 50.dp) // Décalage pour sortir de la carte
                .shadow(100.dp)
                .background(Color(0x80FFA500)) // Ombre en orange, avec transparence
        ) {
            AsyncImage(
                model = restaurant.imageUrl,
                modifier = Modifier.fillMaxSize(), // Prendre toute la taille de la Box
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
            )
        }

        // IconButton positionné en haut à gauche
        Box(
            Modifier
                .align(Alignment.TopStart) // Aligner en haut à gauche
                .padding(8.dp) // Un petit padding pour qu'il ne touche pas les bords
                .shadow(4.dp, shape = CircleShape)
        ) {
            IconButton(
                onClick = { /* Action du bouton */ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .size(50.dp),
            ) {
                Text(
                    "<",
                    style = TextStyle(fontSize = 30.sp),
                    color = Color.Black
                )
            }
        }

        Text(
            text = restaurant.name,
            style = TextStyle(
                fontSize = 50.sp,
                color = Color.Cyan // color : orange
            ), // Utiliser une couleur contrastante
            modifier = Modifier
                .align(Alignment.Center) // Centrer le texte
                .padding(16.dp) // Padding autour du texte
        )

        // Contenu texte sur la gauche
        Column(
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.BottomStart) // Aligner à gauche au centre
        ) {
            Text(
                text = restaurant.speciality,
                style = TextStyle(fontSize = 30.sp, color = Color.DarkGray), // darkblue
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = restaurant.address.toString(),
                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier.padding(top = 4.dp)
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

@Preview
@Composable
fun FilterMenus() {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    val itemPosition = remember {
        mutableIntStateOf(0)
    }

    val usernames = listOf("Cheap", "Expensive")

    Row(Modifier.padding(start = 20.dp, top = 40.dp, bottom = 25.dp)) {
        Text("Short by: ")
        Spacer(Modifier.fillMaxWidth(0.05f)) //Spacer(Modifier.width(5.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        isDropDownExpanded.value = true
                    }
                ) {
                    Text(
                        text = usernames[itemPosition.intValue],
                        style = TextStyle(Color.Magenta)
                    )
                    Spacer(Modifier.fillMaxWidth(0.05f))
                    Image(
                        painter = painterResource(id = R.drawable.expand_arrow),
                        contentDescription = "DropDown Icon",
                        Modifier.size(10.dp)
                    )
                }
                DropdownMenu(
                    expanded = isDropDownExpanded.value,
                    onDismissRequest = {
                        isDropDownExpanded.value = false
                    }) {
                    usernames.forEachIndexed { index, username ->
                        DropdownMenuItem(text = {
                            Text(text = username)
                        },
                            onClick = {
                                isDropDownExpanded.value = false
                                itemPosition.intValue = index
                            })
                    }
                }
            }
        }
    }
}

