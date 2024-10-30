package com.uds.foufoufood.view.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.DrawerScaffold
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel

@Composable
fun RestaurantPage(
    navController: NavHostController,
    restaurants: List<Restaurant>,
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    userViewModel: com.uds.foufoufood.viewmodel.UserViewModel
) {
    DrawerScaffold(
        navController = navController,
        userViewModel = userViewModel,
        currentScreen = Screen.AdminRestaurant.route
    ) {
        Scaffold(topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp, top = 20.dp)
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(Screen.AdminRestaurant.route)
                    }, modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }
            }


        }, floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navigation vers la page pour ajouter un nouveau restaurant
                navController.navigate("addRestaurant")
            }, content = {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un restaurant")
            })
        }) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                items(restaurants.size) { index ->
                    val restaurant = restaurants[index]
                    RestaurantItem(
                        navController, adminRestaurantsViewModel, restaurant = restaurant
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantItem(
    navController: NavController,
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    restaurant: Restaurant
) {
    val context = LocalContext.current
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            // todo : navigate to the (linked restorer and restaurant screen)

            if (restaurant.userId.isEmpty()) {
                adminRestaurantsViewModel.setSelectedRestaurant(restaurant)
                navController.navigate(Screen.AdminLinkARestorerToAResto.route)
            } else Toast.makeText(context, "Restaurant déjà lié", Toast.LENGTH_SHORT).show()

        }) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(text = restaurant.name, style = MaterialTheme.typography.bodyLarge)
            Button(onClick = {
                // todo delete restaurant
                adminRestaurantsViewModel.deleteRestaurant(restaurantId = restaurant._id)
            }) {
                Text(text = "supprimer")
            }
        }
    }
}
