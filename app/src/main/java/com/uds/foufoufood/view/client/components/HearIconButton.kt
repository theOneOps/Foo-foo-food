package com.uds.foufoufood.view.client.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun HeartIconButton() {
    var isFavorite by remember { mutableStateOf(false) }

    IconButton(
        onClick = { isFavorite = !isFavorite },
        modifier = Modifier
            .clip(CircleShape)
            .background(color = Color.Yellow)
            .size(50.dp) // Taille du bouton
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Favorite Icon",
            tint = if (isFavorite) Color.Red else Color.Gray,
            modifier = Modifier.size(30.dp) // Taille de l'icône
        )
    }
}