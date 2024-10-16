package com.uds.foufoufood.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uds.foufoufood.R
import com.uds.foufoufood.model.Category
import com.uds.foufoufood.model.Restaurant
import com.uds.foufoufood.ui.component.CategoryPills
import com.uds.foufoufood.ui.component.RestaurantList
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.viewmodel.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    // Initialize the categories and restaurants here with drawables
    LaunchedEffect(Unit) {
        homeViewModel.initialize(
            categoriesList = listOf(
                Category(1, "Pizza", R.drawable.restaurant),
                Category(2, "Burger", R.drawable.restaurant),
                Category(3, "Mexican", R.drawable.restaurant)
            ),
            restaurantsList = listOf(
                Restaurant(1, "Le Gourmet", "Burger", R.drawable.ph_restaurant),
                Restaurant(2, "Sushi World", "Mexican", R.drawable.ph_restaurant),
                Restaurant(3, "Pizza Palace", "Pizza", R.drawable.ph_restaurant)
            )
        )
    }

    // Use the default SansSerif font family
    val defaultFontFamily = FontFamily.SansSerif

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Title with custom typography
        Text(
            text = "Que souhaitez-vous commander ?",
            style = TextStyle(
                fontFamily = defaultFontFamily,
                fontWeight = FontWeight.Bold, // 700 weight
                fontSize = 30.sp,             // Font size 30px
                lineHeight = 30.sp,           // Line height 30px
                textAlign = TextAlign.Left    // Align text to the left
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        SearchBar(
            searchText = homeViewModel.searchText,
            onSearchTextChanged = homeViewModel::onSearchQueryChanged
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Pills
        CategoryPills(
            categories = homeViewModel.categories,
            selectedCategory = homeViewModel.selectedCategory,
            onCategorySelected = homeViewModel::onCategorySelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Restaurant List
        RestaurantList(restaurants = homeViewModel.filteredRestaurants)
    }
}
