package com.uds.foufoufood.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.R
import kotlin.reflect.KFunction2

@Composable
fun SearchBar(searchText: String, onSearchTextChanged: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        placeholder = { Text("Rechercher...") }, // Placeholder
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(24.dp))
            .background(colorResource(R.color.white_grey), shape = RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        )
    )

}
