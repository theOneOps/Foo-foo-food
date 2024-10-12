package com.uds.foufoufood.ui.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.model.User
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.uds.foufoufood.R
import com.uds.foufoufood.ui.theme.BottomNavBarAdmin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivreurPage(navController: NavHostController, users: List<User>, onRoleChanged: (User, String) -> Unit) {

    var searchText by remember { mutableStateOf("") }

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
                val clientUsers = users.filter { user -> user.role == "Livreur" }

                items(clientUsers.size) { index ->
                    val user = clientUsers[index]
                    UserItem(user = user, onClick = {
                        if (user.id != null && user.id.isNotEmpty()) {
                            navController.navigate("userProfile/${user.id}")
                        } else {
                            // Gérer le cas où l'ID est null ou vide (afficher un message ou ignorer l'action)
                            Log.e("LivreurPage", "L'utilisateur n'a pas d'ID valide")
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




