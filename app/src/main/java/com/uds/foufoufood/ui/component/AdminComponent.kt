package com.uds.foufoufood.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.uds.foufoufood.R

@Composable
fun BottomNavBarAdmin(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.groups),
                        contentDescription = "Clients",
                        modifier = Modifier.size(24.dp) // Limite la taille de l'icône
                    )
                },
                selected = selectedItem == 0,
                onClick = { onItemSelected(0) },
                label = { Text("Clients", fontSize = 12.sp) }
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.delivery),
                        contentDescription = "Livreurs",
                        modifier = Modifier.size(24.dp) // Limite la taille de l'icône
                    )
                },
                selected = selectedItem == 1,
                onClick = { onItemSelected(1) },
                label = { Text("Livreurs", fontSize = 12.sp) }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.cook),
                        contentDescription = "Gérants",
                        modifier = Modifier.size(24.dp) // Limite la taille de l'icône
                    )
                },
                selected = selectedItem == 2,
                onClick = { onItemSelected(2) },
                label = { Text("Gérants", fontSize = 12.sp) }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.restaurant),
                        contentDescription = "Restaurants",
                        modifier = Modifier.size(24.dp) // Limite la taille de l'icône
                    )
                },
                selected = selectedItem == 3,
                onClick = { onItemSelected(3) },
                label = { Text("Restaurants", fontSize = 12.sp) }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_drawer_logout),
                        contentDescription = "Déconnexion",
                        modifier = Modifier.size(24.dp) // Limite la taille de l'icône
                    )
                },
                selected = selectedItem == 4,
                onClick = { onItemSelected(4) },
                label = { Text("Déconnexion", fontSize = 12.sp) }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBarAdmin() {
    TopAppBar(
        title = {
            Text(
                text = "Admin",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* Action du menu */ }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            // Image de profil à droite
            AsyncImage(
                model = R.drawable.full_logo, // URL de l'image de profil
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        },
        modifier = Modifier
            .background(Color.White)
    )
}