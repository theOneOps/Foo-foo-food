//package com.uds.foufoufood.navigation
//
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.composable
//import com.uds.foufoufood.view.admin.ClientScreen
//import com.uds.foufoufood.view.admin.GerantPage
//import com.uds.foufoufood.view.admin.LivreurPage
//import com.uds.foufoufood.viewmodel.AdminUsersViewModel
//
//fun NavGraphBuilder.addAdminUserRoutes(
//    navController: NavHostController,
//    adminUsersViewModel: AdminUsersViewModel
//) {
//    composable(Screen.AdminClient.route) {
//        ClientScreen(
//            navController = navController,
//            adminUsersViewModel = adminUsersViewModel,
//            onRoleChanged = { user, newRole ->
//                adminUsersViewModel.updateUserRole(user, newRole)
//            }
//        )
//    }
//    composable(Screen.AdminLivreur.route) {
//        LivreurPage(
//            navController = navController,
//            adminUsersViewModel = adminUsersViewModel,
//            onRoleChanged = { user, newRole ->
//                adminUsersViewModel.updateUserRole(user, newRole)
//            }
//        )
//    }
//    composable(Screen.AdminGerant.route) {
//        GerantPage(
//            navController = navController,
//            adminUsersViewModel = adminUsersViewModel,
//            onRoleChanged = { user, newRole ->
//                adminUsersViewModel.updateUserRole(user, newRole)
//            }
//        )
//    }
//}
