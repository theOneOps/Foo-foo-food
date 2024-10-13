package com.uds.foufoufood.navigation

import AddRestaurantPage
import Restaurant
import RestaurantPage
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uds.foufoufood.factory.AdminViewModelFactory
import com.uds.foufoufood.model.User
import com.uds.foufoufood.repository.AdminRepository
import com.uds.foufoufood.view.admin.ClientPage
import com.uds.foufoufood.view.admin.GerantPage
import com.uds.foufoufood.view.admin.LivreurPage
import com.uds.foufoufood.view.admin.UserProfileAdminPage
import com.uds.foufoufood.ui.component.BottomNavBarAdmin
import com.uds.foufoufood.ui.component.TopNavBarAdmin
import com.uds.foufoufood.viewmodel.AdminViewModel


@Composable
fun AdminNavHost(navController: NavHostController,
                 adminViewModel: AdminViewModel) {
    //val adminViewModel: AdminViewModel = viewModel(
    //    factory = AdminViewModelFactory(repository)
    //)
    var restaurants = remember { mutableStateListOf(*getRestaurants().toTypedArray()) }
    var selectedItem by remember { mutableIntStateOf(0) }


    // Définit les routes de navigation entre les différentes pages
    Scaffold(
        topBar = { TopNavBarAdmin() },
        bottomBar = {
            BottomNavBarAdmin(selectedItem = selectedItem) { index ->
                selectedItem = index
                when (index) {
                    0 -> navController.navigate(Screen.AdminClient.route)
                    1 -> navController.navigate(Screen.AdminLivreur.route)
                    2 -> navController.navigate(Screen.AdminGerant.route)
                    3 -> navController.navigate(Screen.AdminRestaurant.route)
                }
            }
        }
    ) { paddingValues ->
        NavHost(navController = navController,
            startDestination = Screen.AdminClient.route,
            modifier = Modifier.padding(paddingValues)) {

            // Page principale (ClientPage)
            composable(Screen.AdminClient.route) {
                ClientPage(navController = navController,
                    adminViewModel = adminViewModel,
                    selectedItem = selectedItem, // Passe l'état sélectionné
                    onItemSelected = { selectedItem = it },
                    onRoleChanged = { user, newRole ->
                        adminViewModel.updateUserRole(user, newRole)  // Mise à jour du rôle dans le ViewModel
                    }) // Passe le NavController à ClientPage
            }
            composable(Screen.AdminLivreur.route) {
                LivreurPage(navController = navController,
                    adminViewModel = adminViewModel,
                    onRoleChanged = { user, newRole ->
                        adminViewModel.updateUserRole(user, newRole)  // Mise à jour du rôle dans le ViewModel
                    }) // Passe le NavController à ClientPage
            }
            composable(Screen.AdminGerant.route) {
                GerantPage(navController = navController,
                    adminViewModel = adminViewModel,
                    onRoleChanged = { user, newRole ->
                        adminViewModel.updateUserRole(user, newRole)  // Mise à jour du rôle dans le ViewModel
                    }) // Passe le NavController à ClientPage
            }
            // Nouvelle route pour la page des restaurants
            composable(Screen.AdminRestaurant.route) {
                RestaurantPage(navController = navController, restaurants = restaurants)
            }
            // Page de profil utilisateur
            composable(
                route = Screen.UserProfile.route,
                arguments = listOf(navArgument("userEmail") { type = NavType.StringType })
            ) { backStackEntry ->
                val userEmail = backStackEntry.arguments?.getString("userEmail")
                UserProfileAdminPage(navController = navController,
                    userEmail = userEmail,
                    users = adminViewModel.getAll(),
                    onRoleChanged = { user, newRole ->
                        adminViewModel.updateUserRole(user, newRole)  // Mise à jour du rôle dans le ViewModel
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
