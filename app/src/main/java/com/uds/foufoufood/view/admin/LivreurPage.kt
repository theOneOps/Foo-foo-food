package com.uds.foufoufood.view.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.view.DrawerContent
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import kotlinx.coroutines.launch


@Composable
fun LivreurPage(
    navController: NavHostController,
    adminUsersViewModel: AdminUsersViewModel,
    userViewModel: UserViewModel,
    onRoleChanged: (User, String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        Log.d("ClientPage", "Appel à fetchUsers")
        adminUsersViewModel.fetchUsers("livreur") // AdminViewModel get all users
    }

    val livreurUsers = adminUsersViewModel.filteredUsers
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                closeDrawer = { scope.launch { drawerState.close() } },
                logout = userViewModel::logout,
                userViewModel = userViewModel,
                currentScreen = Screen.AdminLivreur.route
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
                }
            ) { paddingValues ->
                // Contenu principal de la page (la liste des utilisateurs)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues) // Respecter les marges de la TopAppBar
                        .background(Color.White)
                ) {
                    // Search Bar
                    SearchBar(
                        searchText = adminUsersViewModel.searchText,
                        onSearchTextChanged = adminUsersViewModel::onSearchQueryChanged,
                    )
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {

                        if (livreurUsers != null) {
                            items(livreurUsers.size) { index ->
                                val user = livreurUsers[index]
                                UserItem(user = user, onClick = {
                                    if (user.email.isNotEmpty()) {
                                        navController.navigate("userProfile/${user.email}")
                                    } else {
                                        // Gérer le cas où l'ID est null ou vide (afficher un message ou ignorer l'action)
                                        Log.e(
                                            "LivreurPage",
                                            "L'utilisateur n'a pas d'ID valide"
                                        )
                                    }
                                },
                                    onRoleChanged = { newRole ->
                                        onRoleChanged(
                                            user,
                                            newRole
                                        ) // Appelle la fonction onRoleChanged avec l'utilisateur et le nouveau rôle
                                    })
                            }
                        }
                    }
                }
            }
        }
    )
}





