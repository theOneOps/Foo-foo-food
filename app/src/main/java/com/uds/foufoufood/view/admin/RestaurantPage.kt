import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.DrawerScaffold

data class Restaurant(val id: String, val name: String)

@Composable
fun RestaurantPage(
    navController: NavHostController,
    restaurants: SnapshotStateList<com.uds.foufoufood.data_class.model.Restaurant>,
    userViewModel: com.uds.foufoufood.viewmodel.UserViewModel
) {
    DrawerScaffold(
        navController = navController,
        userViewModel = userViewModel,
        currentScreen = Screen.AdminRestaurant.route
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp, top = 20.dp)
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.AdminRestaurant.route)
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // Navigate to the page for adding a new restaurant
                        navController.navigate("addRestaurant")
                    },
                    content = {
                        Icon(Icons.Default.Add, contentDescription = "Ajouter un restaurant")
                    }
                )
            }
        ) { paddingValues ->
            // Display the list of restaurants
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
