package com.uds.foufoufood.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uds.foufoufood.model.User


@Composable
fun UserItem(user: User, modifier: Modifier = Modifier, onClick: () -> Unit, onRoleChanged: (String) -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth() // Prendre toute la largeur disponible
            .padding(6.dp) // Ajouter un padding autour de chaque utilisateur
            .shadow(4.dp, RoundedCornerShape(15.dp)) // Ombre avec coins arrondis
            .background(Color.White, shape = RoundedCornerShape(15.dp)) // Fond blanc avec coins arrondis
            .clickable { onClick() }
            .padding(16.dp), // Padding interne au composant

        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image de l'utilisateur
        AsyncImage(
            model = user.avatarUrl, // Charger l'image à partir de l'URL
            contentDescription = "Avatar de ${user.name}",
            modifier = Modifier
                .size(48.dp) // Taille de l'image
                .clip(RoundedCornerShape(24.dp)) // Image ronde
                .padding(end = 8.dp) // Espacement entre l'image et les informations
        )
        // Informations de l'utilisateur
        Column(
            verticalArrangement = Arrangement.Center // Centrer verticalement
        ) {
            Text(text = user.name, color = Color.Black)
            Text(text = user.email, color = Color.Gray)
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
    }
}