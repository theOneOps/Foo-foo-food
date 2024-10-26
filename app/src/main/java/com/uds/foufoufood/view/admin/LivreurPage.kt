package com.uds.foufoufood.view.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.viewmodel.AdminUsersViewModel


@Composable
fun LivreurPage(
    navController: NavHostController,
    adminUsersViewModel: AdminUsersViewModel,
    onRoleChanged: (User, String) -> Unit
) {

    LaunchedEffect(Unit) {
        Log.d("ClientPage", "Appel à fetchUsers")
        adminUsersViewModel.fetchUsers("livreur") // AdminViewModel get all users
    }

    val livreurUsersState by adminUsersViewModel.filteredUsers.observeAsState()
    val livreurUsers = livreurUsersState!!
    Scaffold(
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

                if (livreurUsers.isNotEmpty()) {
                    items(livreurUsers.size) { index ->
                        val user = livreurUsers[index]
                        UserItem(user = user, onClick = {
                            if (user.email.isNotEmpty()) {
                                navController.navigate("userProfile/${user.email}")
                            } else {
                                // Gérer le cas où l'ID est null ou vide (afficher un message ou ignorer l'action)
                                Log.e("LivreurPage", "L'utilisateur n'a pas d'ID valide")
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
                else
                {
                    item {
                        Box(Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center){
                            Text("il n'y a pas de livreurs !")
                        }
                    }
                }
            }
        }
    }
}




