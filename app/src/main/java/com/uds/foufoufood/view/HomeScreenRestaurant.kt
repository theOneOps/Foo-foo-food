package com.uds.foufoufood.view

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.uds.foufoufood.ui.component.DrawerContent
import com.uds.foufoufood.ui.component.DrawerScaffold
import com.uds.foufoufood.ui.component.RestaurantCard
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.ui.component.SpecialityPills
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.RestaurantViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

@Composable
fun HomeScreenRestaurant(
    navController: NavHostController,
    restaurantViewModel: RestaurantViewModel,
    userViewModel: UserViewModel,
    menuViewModel: MenuViewModel
) {
    val context = LocalContext.current

    val specialities by restaurantViewModel.specialities.observeAsState()
    val selectedSpeciality by restaurantViewModel.selectedSpeciality.observeAsState()
    val filteredRestaurants by restaurantViewModel.filteredRestaurants.observeAsState()

    var textAddress = userViewModel.user.value?.address.toString()
    if (textAddress == "null") {
        textAddress = "Aucune adresse de livraison"
    }

    LaunchedEffect(Unit) {
        restaurantViewModel.initialize(context)
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
                .background(MaterialTheme.colorScheme.background)
        ) {
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
                searchText = restaurantViewModel.searchText,
                onSearchTextChanged = restaurantViewModel::onSearchQueryChangedSpeciality
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                item {
                    SpecialityPills(
                        specialities = specialities ?: emptyList(),
                        selectedSpeciality = selectedSpeciality,
                        onSpecialitySelected = restaurantViewModel::onSpecialitySelected
                    )
                }

                for (restaurant in filteredRestaurants ?: emptyList()) {
                    item {
                        RestaurantCard(navController, menuViewModel, restaurant)
                    }
                }
            }
        }
    }
}

    /*ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                closeDrawer = { scope.launch { drawerState.close() } },
                logout = userViewModel::logout,
                userViewModel = userViewModel,
                currentScreen = Screen.HomeRestaurant.route
            )
        },
        content = {
            Scaffold(
                bottomBar = {
                    BottomNavbarHome(selectedItem = selectedItem) { index ->
                        selectedItem = index
                        when (index) {
                            0 -> navController.navigate(Screen.HomeRestaurant.route)
                            1 -> navController.navigate(Screen.HomeMenu.route)
                        }
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Row at the top with hamburger menu and delivery address centered
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Menu button on the left
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }

                        // Delivery Address centered
                        Text(
                            text = textAddress,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                            color = colorResource(id = R.color.orange),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Page Title
                    Text(
                        text = "Que souhaitez-vous commander ?",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.sofiapro_bold)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            lineHeight = 32.sp,
                            textAlign = TextAlign.Start
                        ),
                        color = colorResource(id = R.color.black),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
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
            Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar
                    SearchBar(
                        searchText = restaurantViewModel.searchText,
                        onSearchTextChanged = restaurantViewModel::onSearchQueryChangedSpeciality
                    )
            // Search Bar
            SearchBar(
                searchText = homeViewModel.searchText,
                onSearchTextChanged = homeViewModel::onSearchQueryChanged
            )

                    Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))

                    // Category Pills and Restaurant List
                    LazyColumn {
                        item {
                            SpecialityPills(
                                specialities = specialities ?: emptyList(),
                                selectedSpeciality = selectedSpeciality,
                                onSpecialitySelected = restaurantViewModel::onSpecialitySelected
                            )
                        }
            // Category Pills and Restaurant List
            LazyColumn {
                // Category Pills
                item {
                    SpecialityPills(
                        specialities = restaurantViewModel.specialities,
                        selectedSpeciality = homeViewModel.selectedSpeciality,
                        onSpecialitySelected = homeViewModel::onSpecialitySelected
                    )
                }

                        for (restaurant in filteredRestaurants ?: emptyList()) {
                            item {
                                RestaurantCard(navController, menuViewModel, restaurant)
                            }
                        }
                    }
                }
            }
        }
    )
}*/
