import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantPage(
    adminRestaurantsViewModel: AdminRestaurantsViewModel,
    navController: NavController,
    restaurants: List<Restaurant>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Liste des Restaurants") },
                navigationIcon = {

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navigation vers la page pour ajouter un nouveau restaurant
                    navController.navigate("addRestaurant")
                },
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter un restaurant")
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            items(restaurants.size) { index ->
                val restaurant = restaurants[index]
                RestaurantItem(navController, adminRestaurantsViewModel, restaurant = restaurant)
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            // todo : navigate to the (linked restorer and restaurant screen)

            if (restaurant.userId.isNullOrEmpty()) {
                adminRestaurantsViewModel.setSelectedRestaurant(restaurant)
                navController.navigate(Screen.AdminLinkARestorerToAResto.route)
            } else
                Toast.makeText(context, "Restaurant déjà lié", Toast.LENGTH_SHORT).show()

        }
    ) {
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
