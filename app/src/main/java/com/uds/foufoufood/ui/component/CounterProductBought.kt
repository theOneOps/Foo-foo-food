package com.uds.foufoufood.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.R

@Composable
fun CounterProductBought(quantity: MutableState<Int>) {
    Row(
        modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        RoundIconButton(isAdd = false,
            colorText = colorResource(R.color.orange),
            colorBack = colorResource(R.color.white),
            colorBorder = colorResource(R.color.orange),
            onClick = { if (quantity.value > 0) quantity.value-- })

        // Display the current quantity
        Text(
            text = quantity.value.toString(),
            modifier = Modifier.padding(horizontal = 24.dp),
            style = TextStyle(
                fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily(
                    Font(R.font.sofiapro_bold)
                )
            )
        )

        RoundIconButton(isAdd = true,
            colorText = colorResource(R.color.white),
            colorBack = colorResource(R.color.orange),
            colorBorder = colorResource(R.color.orange),
            onClick = { quantity.value++ })
    }
}

@Composable
fun RoundIconButton(
    isAdd: Boolean, // true pour "+" et false pour "-"
    colorText: Color, colorBack: Color, colorBorder: Color, onClick: () -> Unit
) {
    var elevation = 0.dp
    if (isAdd) {
        elevation = 6.dp
    }
    Box(
        modifier = Modifier
            .size(48.dp) // Légèrement plus grand que le bouton pour l'ombre
            .shadow(
                elevation = elevation, // Ajustez la taille de l'ombre
                shape = CircleShape,
                ambientColor = colorResource(R.color.orange_alpha), // Couleur orange
                spotColor = colorResource(R.color.orange) // Couleur orange
            ), contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .background(color = colorText, shape = CircleShape)
                .border(width = 1.dp, color = colorBorder, shape = CircleShape)
                .clip(CircleShape),
            shape = CircleShape,
            color = colorBack, // Couleur du bouton
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = if (isAdd) Icons.Default.Add else Icons.Default.Remove,
                    contentDescription = if (isAdd) "Add" else "Remove",
                    tint = colorText, // Couleur de l'icône
                    modifier = Modifier.size(24.dp) // Taille de l'icône
                )
            }
        }
    }
}
