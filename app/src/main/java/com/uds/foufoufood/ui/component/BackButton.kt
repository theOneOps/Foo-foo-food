package com.uds.foufoufood.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uds.foufoufood.R


@Composable
fun BackButton(navController: NavController) {
    // Bouton de retour en haut à gauche
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        IconButton(
            onClick = { navController.popBackStack() }, // Action de retour
            modifier = Modifier
                .shadow(8.dp, shape = RoundedCornerShape(12.dp))
                .align(Alignment.TopStart)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .size(42.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Retour",
                tint = colorResource(id = R.color.orange),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}