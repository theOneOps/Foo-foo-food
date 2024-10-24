import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uds.foufoufood.data_class.model.Restaurant

data class Restaurant(val id: String, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantPage(navController: NavController, restaurants: SnapshotStateList<com.uds.foufoufood.data_class.model.Restaurant>) {
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
                RestaurantItem(restaurant = restaurant)
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = restaurant.name, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
