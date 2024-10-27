package com.uds.foufoufood.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.uds.foufoufood.R
import com.uds.foufoufood.data_class.model.Speciality
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.DrawerScaffold
import com.uds.foufoufood.ui.component.RestaurantCard
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.ui.component.SpecialityPills
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    userViewModel: UserViewModel,
    menuViewModel: MenuViewModel
) {
    // Initialize the categories and restaurants here with drawables
    LaunchedEffect(Unit) {
        homeViewModel.initialize(
            specialityList = listOf(
                Speciality(1, "Pizza", R.drawable.ic_category_pizza),
                Speciality(2, "Burger", R.drawable.ic_category_burger),
                Speciality(3, "Mexican", R.drawable.ic_category_mexican),
                Speciality(4, "Pizza", R.drawable.ic_category_pizza),
                Speciality(5, "Burger", R.drawable.ic_category_burger),
                Speciality(6, "Pizza", R.drawable.ic_category_pizza),
                Speciality(7, "Burger", R.drawable.ic_category_burger),

                )
        )
    }

    DrawerScaffold(
        navController = navController,
        userViewModel = userViewModel,
        currentScreen = Screen.Home.route
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Title and menu button at the top
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Que souhaitez-vous commander ?",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.sofiapro_regular)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        lineHeight = 30.sp,
                        textAlign = TextAlign.Left
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            SearchBar(
                searchText = homeViewModel.searchText,
                onSearchTextChanged = homeViewModel::onSearchQueryChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Pills and Restaurant List
            LazyColumn {
                // Category Pills
                item {
                    SpecialityPills(
                        specialities = homeViewModel.specialities,
                        selectedSpeciality = homeViewModel.selectedSpeciality,
                        onSpecialitySelected = homeViewModel::onSpecialitySelected
                    )
                }

                // Restaurant List
                items(homeViewModel.filteredRestaurants) { restaurant ->
                    RestaurantCard(
                        navHostController = navController,
                        menuViewModel = menuViewModel,
                        restaurant = restaurant,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}



