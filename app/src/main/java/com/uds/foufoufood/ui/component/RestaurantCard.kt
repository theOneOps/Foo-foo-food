package com.uds.foufoufood.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.uds.foufoufood.R
import com.uds.foufoufood.activities.main.TokenManager.getUserId
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.UserViewModel


//@Composable
//fun RestaurantList(
//    navHostController: NavHostController,
//    menuViewModel: MenuViewModel,
//    restaurants: List<Restaurant>
//) {
//    LazyColumn {
//        items(restaurants) { restaurant ->
//            RestaurantCard(navHostController, menuViewModel, restaurant)
//        }
//    }
//}

// todo: ajouter le modifier.clickable sur cette card pour diriger l'utilisateur
//  sur le screen du restaurant correspondant avec ses menus
@Composable
fun RestaurantCard(
    navHostController: NavHostController,
    menuViewModel: MenuViewModel,
    restaurant: Restaurant,
    userViewModel: UserViewModel,
) {

    // État pour contrôler l'affichage du dialog
    val openDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userId = getUserId(context)

    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            menuViewModel.setSharedRestaurant(restaurant)
            navHostController.navigate(Screen.ClientRestaurantAllMenusPage.route)
        }
        .padding(8.dp), // Optional padding to space between cards
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                // image from url
                model = restaurant.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop // Make sure image fills the entire area
            )

            // Restaurant name and category section
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                // Restaurant name
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Category as a tag with a grey background
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                colorResource(R.color.white_grey), shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = restaurant.speciality,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorResource(R.color.grey),
                            fontSize = 12.sp,
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // todo : add the textlink for the modify link
                        //  (modify restaurant's intel), only the owner of
                        //  the restaurant has the privilege
                        if (userId == restaurant.userId)
                            TextLink(label="Modify restaurant", onClick = {
                                menuViewModel.setSharedRestaurant(restaurant)
                                navHostController.navigate(Screen.ModifyRestaurantPage.route)
                            })
                    }
                }
            }
        }
    }
}
