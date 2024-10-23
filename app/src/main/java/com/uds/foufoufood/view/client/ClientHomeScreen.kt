package com.uds.foufoufood.view.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Restaurant


val restaurantTest = Restaurant(
    name = "Le Gourmet",
    address = Address("123 Rue de Paris", "75000", "Paris", "France"),
    speciality = "French cuisine",
    phone = "0123456789",
    openingHours = "09:00 - 22:00",
    items = listOf(
        Menu(
            name = "Coq au Vin",
            descriptor = "Classic French chicken dish cooked with wine",
            price = 25.0,
            category = "Main course",
            image = "https://images.unsplash.com/photo-1468070975228-085c1fdd2d3e?q=80&w=1973&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            restaurantId = "1"
        ), Menu(
            name = "Crème Brûlée",
            descriptor = "Rich custard base topped with a layer of caramelized sugar",
            price = 10.0,
            category = "Dessert",
            image = "https://images.unsplash.com/photo-1487004121828-9fa15a215a7a?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            restaurantId = "1"
        )
    ),
    rating = 4.7,
    reviews = listOf("Delicious food!", "Amazing ambiance."),
    imageUrl = "https://images.unsplash.com/photo-1498654896293-37aacf113fd9?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
)

val menuTest = Menu(
    name = "Crème Brûlée",
    descriptor = "Rich custard base topped with a layer of caramelized sugar",
    price = 10.0,
    category = "Dessert",
    image = "https://images.unsplash.com/photo-1487004121828-9fa15a215a7a?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    restaurantId = "1"
)

@Composable
fun ClientHomeScreen(navHostController: NavHostController) {
    Column {
        RestaurantComponent()
        Spacer(Modifier.fillMaxHeight(0.05f))
        FilterMenus()
        PrintAllMenus()
    }
}

// test restaurants avec des menus
@Preview
@Composable
fun RestaurantComponent(restaurant: Restaurant = restaurantTest) {
    LazyColumn(Modifier.padding(2.dp)) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
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
                        .shadow(
                            100.dp,
                        )
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
    }
}

@Preview
@Composable
fun MenuComponent(menu: Menu = menuTest) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            // Image en haut
            AsyncImage(
                model = menu.image,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Hauteur de l'image définie
                    .clip(RoundedCornerShape(16.dp)),
                contentDescription = menu.descriptor,
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
                Column {
                    Text(menu.name, style = TextStyle(Color.Black, fontSize = 30.sp))
                    Text(menu.descriptor, style = TextStyle(Color.Gray, fontSize = 20.sp))
                }
            }
        }
    }
}

@Preview
@Composable
fun PrintAllMenus(restaurant: Restaurant = restaurantTest) {
    restaurant.items.forEach { e ->
        MenuComponent(e)
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

    val usernames = listOf("Popular", "Most rated", "Cheap", "Expensive")

    Row(Modifier.padding(start = 20.dp)) {
        Text("Short by: ")
        Spacer(Modifier.fillMaxWidth(0.05f))
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

