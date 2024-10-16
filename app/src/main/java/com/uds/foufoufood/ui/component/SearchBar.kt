package com.uds.foufoufood.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
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
            .background(Color.LightGray, shape = RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
