package com.uds.foufoufood.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun CounterProductBought() {
    var stateCount by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bouton pour décrémenter
        IconButton(
            onClick = { if (stateCount > 0) stateCount-- },
            modifier = Modifier
                .size(40.dp)
                .shadow(4.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center  // Centre le contenu
            ) {
                Text("-", style = TextStyle(Color.Red, fontSize = 25.sp))
            }
        }

        // Texte du compteur au centre
        Text(
            text = stateCount.toString(),
            modifier = Modifier.padding(horizontal = 24.dp),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )

        // Bouton pour incrémenter
        IconButton(
            onClick = { stateCount++ },
            modifier = Modifier
                .size(40.dp)
                .shadow(4.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(Color.Red)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center  // Centre le contenu
            ) {
                Text("+", style = TextStyle(Color.White, fontSize = 25.sp))
            }
        }
    }
}
