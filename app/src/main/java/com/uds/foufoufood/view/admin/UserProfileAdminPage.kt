package com.uds.foufoufood.view.admin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.uds.foufoufood.data_class.model.User
import com.uds.foufoufood.viewmodel.AdminUsersViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileAdminPage(
    adminUsersViewModel: AdminUsersViewModel,
    navController: NavController,
    userEmail: String?,
    users: List<User>,
    onRoleChanged: (User, String) -> Unit
) {
    val context = LocalContext.current
    if (userEmail != null) {
        val user = users.find { it.email == userEmail }
        var selectedRole by remember { mutableStateOf(user?.role ?: "Client") }
        var showDropdown by remember { mutableStateOf(false) }

        var isBlocked by remember { mutableStateOf(user?.blockedAccount) }

        if (user != null) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = "Profil Utilisateur") }, navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour"
                        )
                    }
                })
            }) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = user.avatarUrl),
                        contentDescription = "Photo de profil",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                    Text(text = user.email, fontSize = 16.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Changer le rôle", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { showDropdown = true }, modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = selectedRole)
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Changer le rôle")
                        }

                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false }) {
                            DropdownMenuItem(text = { Text("Client") }, onClick = {
                                selectedRole = "client"
                                onRoleChanged(user, "client")
                                showDropdown = false
                            })
                            DropdownMenuItem(text = { Text("Livreur") }, onClick = {
                                selectedRole = "livreur"
                                onRoleChanged(user, "livreur")
                                showDropdown = false
                            })
                            DropdownMenuItem(text = { Text("Restaurateur") }, onClick = {
                                selectedRole = "restaurateur"
                                onRoleChanged(user, "restaurateur")
                                showDropdown = false
                            })
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

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

                    Button(
                        onClick = {
                            if (isBlocked == true) {
                                adminUsersViewModel.unlockAccount(user._id)
                            } else {
                                adminUsersViewModel.blockAccount(user._id)
                            }
                            isBlocked = !isBlocked!! // Mise à jour immédiate de l'état local
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isBlocked == true) "Activer le compte" else "Désactiver le compte",
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            adminUsersViewModel.deleteAccount(user._id)
                            navController.popBackStack()
                            Toast.makeText(
                                context,
                                "Compte supprimé avec succès",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Supprimer le compte", color = Color.White)
                    }
                }
            }
        } else {
            Text("Utilisateur non trouvé")
        }
    }
}