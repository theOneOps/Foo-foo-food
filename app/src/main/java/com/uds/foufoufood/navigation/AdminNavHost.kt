//package com.uds.foufoufood.navigation
//
//import AddRestaurantPage
//import RestaurantPage
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.navArgument
//import com.uds.foufoufood.view.admin.UserProfileAdminPage
//import com.uds.foufoufood.ui.component.BottomNavBarAdmin
//import com.uds.foufoufood.ui.component.TopNavBarAdmin
//import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
//import com.uds.foufoufood.viewmodel.AdminUsersViewModel
//import com.uds.foufoufood.data_class.model.Restaurant
//
//
//@Composable
//fun AdminNavHost(
//    navController: NavHostController,
//    adminUsersViewModel: AdminUsersViewModel,
//    adminRestaurantsViewModel: AdminRestaurantsViewModel
//) {
//    var selectedItem by remember { mutableIntStateOf(0) }
//
//
//    // Définit les routes de navigation entre les différentes pages
//    Scaffold(
//        topBar = { TopNavBarAdmin() },
//        bottomBar = {
//            BottomNavBarAdmin(selectedItem = selectedItem) { index ->
//                selectedItem = index
//                when (index) {
//                    0 -> navController.navigate(Screen.AdminClient.route)
//                    1 -> navController.navigate(Screen.AdminLivreur.route)
//                    2 -> navController.navigate(Screen.AdminGerant.route)
//                    3 -> navController.navigate(Screen.AdminRestaurant.route)
//                }
//            }
//        }
//    ) { paddingValues ->
//        NavHost(
//            navController = navController,
//            startDestination = Screen.AdminClient.route,
//            modifier = Modifier.padding(paddingValues)
//        ) {
//
//            // Ajouter les routes utilisateur
//            addAdminUserRoutes(navController, adminUsersViewModel)
//
//            // Nouvelle route pour la page des restaurants
//            composable(Screen.AdminRestaurant.route) {
//                RestaurantPage(navController = navController, restaurants = adminRestaurantsViewModel.restaurants)
//            }
//            // Page de profil utilisateur
//            composable(
//                route = Screen.UserProfile.route,
//                arguments = listOf(navArgument("userEmail") { type = NavType.StringType })
//            ) { backStackEntry ->
//                val userEmail = backStackEntry.arguments?.getString("userEmail")
//                UserProfileAdminPage(navController = navController,
//                    userEmail = userEmail,
//                    users = adminUsersViewModel.getAll(),
//                    onRoleChanged = { user, newRole ->
//                        adminUsersViewModel.updateUserRole(
//                            user,
//                            newRole
//                        )  // Mise à jour du rôle dans le ViewModel
//                    })
//            }
//            composable("addRestaurant") {
//                AddRestaurantPage(
//                    navController = navController,
//                    onRestaurantAdded = { newRestaurant : Restaurant->
//                        adminRestaurantsViewModel.addRestaurant(newRestaurant)
//                    }
//                )
//            }
//
//        }
//    }
//
//}
//
