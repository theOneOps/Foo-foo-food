import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.view.DrawerContent
import kotlinx.coroutines.launch

data class Restaurant(val id: String, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantPage(navController: NavHostController,
                   restaurants: SnapshotStateList<com.uds.foufoufood.data_class.model.Restaurant>,
                     userViewModel: com.uds.foufoufood.viewmodel.UserViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                closeDrawer = { scope.launch { drawerState.close() } },
                logout = userViewModel::logout,
                userViewModel = userViewModel,
                currentScreen = Screen.AdminRestaurant.route
            )
        },
        content = {
            Scaffold(
                topBar = {
                    // Ajuste la taille du bouton de menu pour éviter de couvrir le contenu principal
                    Box(
                        modifier = Modifier
                            .fillMaxWidth() // Utilise seulement la largeur pour le placer en haut
                            .padding(end = 20.dp, top = 20.dp)
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            },
                            modifier = Modifier.align(Alignment.TopEnd) // Aligne en haut à droite
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
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
    )
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
