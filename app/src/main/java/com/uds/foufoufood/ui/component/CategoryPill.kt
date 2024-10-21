package com.uds.foufoufood.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.FontScaling
import androidx.compose.ui.unit.dp
import com.uds.foufoufood.R
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
        color = if (isSelected) colorResource(id = R.color.orange)
        else colorResource(id = R.color.white), // Use colors from resources
        shape = RoundedCornerShape(24.dp), // Rounded pill shape
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .shadow(4.dp, RoundedCornerShape(48.dp)), // Elevation with rounded shape
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .width(54.dp)
                .height(90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Round Icon
            Image(
                painter = painterResource(id = category.iconResId),
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp) // Icon size
                    .clip(CircleShape), // Make the icon round
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category name
            Text(
                text = category.name,
                color = if (isSelected) colorResource(id = R.color.white)
                else colorResource(id = R.color.black), // Text color based on selection
                style = MaterialTheme.typography.bodySmall,

            )
        }
    }
}
