package com.uds.foufoufood.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.model.User
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileAdminPage(navController: NavController, userId: String?,users :List<User>, onRoleChanged: (String) -> Unit) {
    if (userId != null) {
        // Récupérer les détails de l'utilisateur
        val user = users.find { it.id == userId }
        var selectedRole by remember { mutableStateOf(user?.role ?: "Client") }
        var showDropdown by remember { mutableStateOf(false) }

        if (user != null) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Profil Utilisateur") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Image de profil centrée
                    Image(
                        painter = rememberAsyncImagePainter(model = user.avatarUrl),
                        contentDescription = "Photo de profil",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Nom de l'utilisateur
                    Text(text = user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                    // Email de l'utilisateur
                    Text(text = user.email, fontSize = 16.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sélecteur de rôle
                    Text(text = "Changer le rôle", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { showDropdown = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = selectedRole)
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Changer le rôle")
                        }

                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Client") },
                                onClick = {
                                    selectedRole = "Client"
                                    onRoleChanged("Client")
                                    showDropdown = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Livreur") },
                                onClick = {
                                    selectedRole = "Livreur"
                                    onRoleChanged("Livreur")
                                    showDropdown = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Restaurateur") },
                                onClick = {
                                    selectedRole = "Restaurateur"
                                    onRoleChanged("Restaurateur")
                                    showDropdown = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Afficher le bouton Associer si le rôle est Restaurateur
                    if (selectedRole == "Restaurateur") {
                        Button(
                            onClick = {
                                navController.navigate("restaurantList")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Associer à un restaurant", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Bouton pour désactiver le compte
                    Button(
                        onClick = {
                            // Logique pour désactiver le compte
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Désactiver le compte", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bouton pour supprimer le compte
                    Button(
                        onClick = {
                            // Logique pour supprimer le compte
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Supprimer le compte", color = Color.White)
                    }
                }
            }
        } else {
            Text("Utilisateur introuvable")
        }
    } else {
        Text("ID utilisateur manquant")
    }
}