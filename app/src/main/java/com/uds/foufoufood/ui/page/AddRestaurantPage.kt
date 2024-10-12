import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRestaurantPage(navController: NavController, onRestaurantAdded: (Restaurant) -> Unit) {
    var restaurantName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Ajouter un restaurant") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = restaurantName,
                onValueChange = { restaurantName = it },
                label = { Text("Nom du restaurant") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (restaurantName.isNotEmpty()) {
                        // Créer un nouvel objet restaurant et le passer à la fonction de callback
                        val newRestaurant = Restaurant(id = (0..1000).random().toString(), name = restaurantName)
                        onRestaurantAdded(newRestaurant)
                        navController.popBackStack() // Retour à la liste des restaurants après l'ajout
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Ajouter")
            }
        }
    }
}
