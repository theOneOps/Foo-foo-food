package com.uds.foufoufood.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.model.Category

@Composable
fun CategoryPills(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    LazyRow {
        items(categories) { category ->
            CategoryPill(
                category = category,
                isSelected = selectedCategory == category,
                onClick = {
                    if (selectedCategory == category) {
                        onCategorySelected(null)
                    } else {
                        onCategorySelected(category)
                    }
                }
            )
        }
    }
}

@Composable
fun CategoryPill(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) Color.Blue else Color.LightGray,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .shadow(8.dp, RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = category.name)
        }
    }
}
