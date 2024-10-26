package com.uds.foufoufood.view.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.viewmodel.AdminUsersViewModel


@Composable
fun ClientScreen(
    navController: NavHostController,
    adminUsersViewModel: AdminUsersViewModel,
    onRoleChanged: (User, String) -> Unit
) {
    LaunchedEffect(Unit) {
        Log.d("ClientPage", "Appel à fetchUsers")
        adminUsersViewModel.fetchUsers("client") // AdminViewModel get all users
    }
    adminUsersViewModel.filterUsers("client")
    val clientUsersState by adminUsersViewModel.filteredUsers.observeAsState()
    val clientUsers = clientUsersState!!
    Scaffold()
    { paddingValues ->
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

            if (clientUsers.isEmpty()) {
                Text("Aucun client trouvé.")
                Log.d("ClientPage", "Aucun utilisateur trouvé.")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {

                    items(clientUsers.size) { index ->
                        val user = clientUsers[index]
                        UserItem(user = user, onClick = {
                            if (user.email.isNotEmpty()) {
                                navController.navigate("userProfile/${user.email}")
                            } else {
                                Log.e("ClientPage", "L'utilisateur n'a pas d'email valide")
                            }
                        },
                            onRoleChanged = { newRole ->
                                onRoleChanged(
                                    user,
                                    newRole
                                ) // Appelle la fonction onRoleChanged avec l'utilisateur
                                // et le nouveau rôle
                            })
                    }
                }
            }
        }
    }
}




