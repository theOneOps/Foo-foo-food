package com.uds.foufoufood.view.admin

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
import com.uds.foufoufood.data_class.model.User

@Composable
fun UserItem(
    user: User, modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(6.dp)
        .shadow(4.dp, RoundedCornerShape(15.dp))
        .background(
            Color.White, shape = RoundedCornerShape(15.dp)
        )
        .clickable { onClick() }
        .padding(16.dp),

        verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "Avatar de ${user.name}",
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .padding(end = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = user.name, color = Color.Black)
            Text(text = user.email, color = Color.Gray)
        }

        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next"
        )
    }
}