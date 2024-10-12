package com.uds.foufoufood.ui.page


import AddRestaurantPage
import Restaurant
import RestaurantPage
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uds.foufoufood.model.User
import com.uds.foufoufood.ui.theme.BottomNavBarAdmin
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import com.uds.foufoufood.ui.theme.TopNavBarAdmin


@Composable
fun AdminNavHost(navController: NavHostController) {
    var restaurants = remember { mutableStateListOf(*getRestaurants().toTypedArray()) }
    var selectedItem by remember { mutableIntStateOf(0)}
    var users = mutableListOf(
        User(
            "1",
            "Alyce Lambo",
            "alyce.lambo@gmail.com",
            "https://i.pravatar.cc/150?img=1",
            "Client"
        ),
        User(
            "2",
            "John Doe",
            "john.doe@example.com",
            "https://i.pravatar.cc/150?img=2",
            "Restaurateur"
        ),
        User(
            "3",
            "Jane Smith",
            "jane.smith@example.com",
            "https://i.pravatar.cc/150?img=3",
            "Client"
        ),
        User(
            "4",
            "Michael Brown",
            "michael.brown@example.com",
            "https://i.pravatar.cc/150?img=4",
            "Livreur"
        ),
        User(
            "5",
            "Emily White",
            "emily.white@example.com",
            "https://i.pravatar.cc/150?img=5",
            "Restaurateur"
        ),
        User(
            "6",
            "Christopher Black",
            "chris.black@example.com",
            "https://i.pravatar.cc/150?img=6",
            "Client"
        ),
        User(
            "7",
            "David Green",
            "david.green@example.com",
            "https://i.pravatar.cc/150?img=7",
            "Livreur"
        ),
        User(
            "8",
            "Sophia Blue",
            "sophia.blue@example.com",
            "https://i.pravatar.cc/150?img=8",
            "Restaurateur"
        ),
        User(
            "9",
            "Oliver Red",
            "oliver.red@example.com",
            "https://i.pravatar.cc/150?img=9",
            "Client"
        ),
        User(
            "10",
            "Isabella Yellow",
            "isabella.yellow@example.com",
            "https://i.pravatar.cc/150?img=10",
            "Livreur"
        ),
        User(
            "11",
            "Liam Purple",
            "liam.purple@example.com",
            "https://i.pravatar.cc/150?img=11",
            "Restaurateur"
        ),
        User(
            "12",
            "Emma Pink",
            "emma.pink@example.com",
            "https://i.pravatar.cc/150?img=12",
            "Client"
        ),
        User(
            "13",
            "Noah Grey",
            "noah.grey@example.com",
            "https://i.pravatar.cc/150?img=13",
            "Livreur"
        ),
        User(
            "14",
            "Mia Silver",
            "mia.silver@example.com",
            "https://i.pravatar.cc/150?img=14",
            "Restaurateur"
        ),
        User(
            "15",
            "Lucas Gold",
            "lucas.gold@example.com",
            "https://i.pravatar.cc/150?img=15",
            "Client"
        )
    )
    // Définit les routes de navigation entre les différentes pages
    Scaffold(
        topBar = { TopNavBarAdmin()},
        bottomBar = {
            BottomNavBarAdmin(selectedItem = selectedItem) { index ->
                selectedItem = index
                when (index) {
                    0 -> navController.navigate("clientPage")
                    1 -> navController.navigate("livreurPage")
                    2 -> navController.navigate("gerantPage")
                    3 -> navController.navigate("restaurantPage")
                }
            }
        }
    ) { paddingValues ->
        NavHost(navController = navController,
            startDestination = "clientPage",
            modifier = Modifier.padding(paddingValues)) {

            // Page principale (ClientPage)
            composable("clientPage") {
                ClientPage(navController = navController,
                    users = users,
                    selectedItem = selectedItem, // Passe l'état sélectionné
                    onItemSelected = { selectedItem = it },
                    onRoleChanged = { user, newRole ->
                        // Logique pour mettre à jour le rôle de l'utilisateur
                        val index = users.indexOf(user)
                        if (index != -1) {
                            users[index] = user.copy(role = newRole)
                        }
                    }) // Passe le NavController à ClientPage
            }
            composable("livreurPage") {
                LivreurPage(navController = navController,
                    users = users,
                    onRoleChanged = { user, newRole ->
                        // Logique pour mettre à jour le rôle de l'utilisateur
                        val index = users.indexOf(user)
                        if (index != -1) {
                            users[index] = user.copy(role = newRole)
                            // Par exemple, enregistrer le changement dans la BDD ou l'état de l'application
                        }
                    }) // Passe le NavController à ClientPage
            }
            composable("gerantPage") {
                GerantPage(navController = navController,
                    users = users,
                    onRoleChanged = { user, newRole ->
                        // Logique pour mettre à jour le rôle de l'utilisateur
                        val index = users.indexOf(user)
                        if (index != -1) {
                            users[index] = user.copy(role = newRole)
                            // Par exemple, enregistrer le changement dans la BDD ou l'état de l'application
                        }
                    }) // Passe le NavController à ClientPage
            }
            // Nouvelle route pour la page des restaurants
            composable("restaurantPage") {
                RestaurantPage(navController = navController, restaurants = restaurants)
            }
            // Page de profil utilisateur
            composable(
                route = "userProfile/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                UserProfileAdminPage(navController = navController,
                    userId = userId,
                    users = users,
                    onRoleChanged = { newRole ->
                        val user = users.find { it.id == userId }
                        if (user != null) {
                            user.role = newRole
                            // Autres actions si nécessaire, comme enregistrer le changement en BDD
                        }
                    })
            }
            composable("addRestaurant") {
                AddRestaurantPage(
                    navController = navController,
                    onRestaurantAdded = { newRestaurant ->
                        // Ajouter le nouveau restaurant à la liste
                        restaurants.add(newRestaurant)
                    }
                )
            }

        }
    }

}
fun getRestaurants() = listOf(
    Restaurant("1", "Restaurant A"),
    Restaurant("2", "Restaurant B"),
    Restaurant("3", "Restaurant C"),
    Restaurant("4", "Restaurant D"),
    Restaurant("5", "Restaurant E")
)