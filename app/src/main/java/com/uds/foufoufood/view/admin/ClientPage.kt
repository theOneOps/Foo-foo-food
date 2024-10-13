package com.uds.foufoufood.view.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.model.User
import androidx.navigation.NavHostController
import com.uds.foufoufood.viewmodel.AdminViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientPage(
    navController: NavHostController,
    adminViewModel: AdminViewModel,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    onRoleChanged: (User, String) -> Unit
) {

    var searchText by remember { mutableStateOf("") }
    // Observer les utilisateurs
    val users by adminViewModel.users.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        Log.d("ClientPage", "Appel à fetchUsers")
        adminViewModel.fetchUsers()
    }

    val clientUsers = users?.filter { it.role == "client" }
    Scaffold()
     { paddingValues ->
        // Contenu principal de la page (la liste des utilisateurs)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Respecter les marges de la TopAppBar
                .background(Color.White)
        ) {
            // Barre de recherche
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Rechercher un utilisateur") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Recherche")
                }
            )
            if (clientUsers != null) {
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
                                if (user.email != null && user.email.isNotEmpty()) {
                                    navController.navigate("userProfile/${user.email}")
                                } else {
                                    Log.e("ClientPage", "L'utilisateur n'a pas d'email valide")
                                }
                            },
                                onRoleChanged = { newRole ->
                                    onRoleChanged(user, newRole) // Appelle la fonction onRoleChanged avec l'utilisateur et le nouveau rôle
                                })
                        }
                    }
                }
            }
        }
    }
}




