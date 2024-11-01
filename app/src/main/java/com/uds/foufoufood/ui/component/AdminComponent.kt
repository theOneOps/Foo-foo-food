package com.uds.foufoufood.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.R

@Composable
fun BottomNavBarAdmin(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White, contentColor = Color.Black
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            NavigationBarItem(icon = {
                Icon(
                    painter = painterResource(id = R.drawable.groups),
                    contentDescription = "Clients",
                    modifier = Modifier.size(24.dp)
                )
            },
                selected = selectedItem == 0,
                onClick = { onItemSelected(0) },
                label = { Text("Clients", fontSize = 12.sp) })

            NavigationBarItem(icon = {
                Icon(
                    painter = painterResource(id = R.drawable.delivery),
                    contentDescription = "Livreurs",
                    modifier = Modifier.size(24.dp)
                )
            },
                selected = selectedItem == 1,
                onClick = { onItemSelected(1) },
                label = { Text("Livreurs", fontSize = 12.sp) })
            NavigationBarItem(icon = {
                Icon(
                    painter = painterResource(id = R.drawable.cook),
                    contentDescription = "Gérants",
                    modifier = Modifier.size(24.dp)
                )
            },
                selected = selectedItem == 2,
                onClick = { onItemSelected(2) },
                label = { Text("Gérants", fontSize = 12.sp) })
            NavigationBarItem(icon = {
                Icon(
                    painter = painterResource(id = R.drawable.restaurant),
                    contentDescription = "Restaurants",
                    modifier = Modifier.size(24.dp)
                )
            },
                selected = selectedItem == 3,
                onClick = { onItemSelected(3) },
                label = { Text("Restaurants", fontSize = 12.sp) })
        }
    }
}