package com.uds.foufoufood.view.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.navigation.NavHostController
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.viewmodel.AdminViewModel


@Composable
fun LivreurPage(
    navController: NavHostController,
    adminViewModel: AdminViewModel,
    onRoleChanged: (User, String) -> Unit
) {

    var searchText by remember { mutableStateOf("") }

    val users by adminViewModel.users.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        Log.d("ClientPage", "Appel à fetchUsers")
        adminViewModel.fetchUsers()
    }

    val livreurUsers = users?.filter { it.role == "livreur" }
    Scaffold(
    ) { paddingValues ->
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
            }
        }
    }
}




