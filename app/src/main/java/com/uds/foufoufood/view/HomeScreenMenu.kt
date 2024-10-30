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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.DrawerScaffold
import com.uds.foufoufood.ui.component.MenuComponent
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.ui.component.SpecialityPills
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.RestaurantViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun HomeScreenMenu(
    navController: NavHostController,
    restaurantViewModel: RestaurantViewModel,
    userViewModel: UserViewModel,
    menuViewModel: MenuViewModel
) {
    // State for controlling drawer
    val categories by menuViewModel.categories.observeAsState()
    val filteredMenu by menuViewModel.filteredMenu.observeAsState()
    val selectedCategory by menuViewModel.selectedCategory.observeAsState()

    val idRestaurantOfConnectedUser by restaurantViewModel.idRestaurantOfConnectedUser.observeAsState()

    var textAddress = userViewModel.user.value?.address.toString()
    if (textAddress == "null") {
        textAddress = "Aucune adresse de livraison"
    }

    // Initialize the categories and restaurants here with drawables
    LaunchedEffect(Unit) {
        menuViewModel.initialize()
    }

    DrawerScaffold(
        navController = navController,
        userViewModel = userViewModel,
        currentScreen = Screen.HomeRestaurant.route
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = colorResource(id = R.color.white))
        ) {
            Spacer(modifier = Modifier.height(23.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = textAddress,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Que souhaitez-vous commander ?",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.sofiapro_bold)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    lineHeight = 32.sp,
                    textAlign = TextAlign.Start
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            SearchBar(
                searchText = menuViewModel.searchTextCategory,
                onSearchTextChanged = menuViewModel::onSearchQueryChangedCategory
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Pills and Restaurant List
            LazyColumn {
                // Category Pills
                item {
                    SpecialityPills(
                        specialities = categories ?: emptyList(),
                        selectedSpeciality = selectedCategory,
                        onSpecialitySelected = menuViewModel::onCategorySelected
                    )
                }

                // Restaurant List
                for (menu in filteredMenu ?: emptyList()) {
                    // si l'utilisateur est connecté en tant que restaurateur, on affiche le bouton de suppression
                    if (idRestaurantOfConnectedUser == menu.restaurantId) {
                        menuViewModel.setIsConnectedRestorer(true)
                    } else {
                        menuViewModel.setIsConnectedRestorer(false)
                    }
                    item {
                        MenuComponent(
                            navController = navController,
                            menu = menu,
                            menuViewModel = menuViewModel
                        )
                    }
                }
            }
        }
    }
}
