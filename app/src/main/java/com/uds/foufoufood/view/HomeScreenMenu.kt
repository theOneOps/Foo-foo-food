package com.uds.foufoufood.view

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
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
import com.uds.foufoufood.ui.component.MenuComponent
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.ui.component.SpecialityPills
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.RestaurantViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreenMenu(
    navController: NavHostController,
    restaurantViewModel: RestaurantViewModel,
    userViewModel: UserViewModel,
    menuViewModel: MenuViewModel
) {
    // State for controlling drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedItem by remember { mutableIntStateOf(1) }

    val categories by menuViewModel.categories.observeAsState()
    val filteredMenu by menuViewModel.filteredMenu.observeAsState()
    val selectedCategory by menuViewModel.selectedCategory.observeAsState()

    val idRestaurantOfConnectedUser by restaurantViewModel.idRestaurantOfConnectedUser.observeAsState()

    // Initialize the categories and restaurants here with drawables
    LaunchedEffect(Unit) {
        menuViewModel.initialize()
    }

    // Modal navigation drawer
    ModalNavigationDrawer(
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
                        selectedItem = index // Met à jour l'élément sélectionné
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
                        .padding(innerPadding) // Apply innerPadding here
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Row at the top with title and menu button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Title
                        Text(
                            text = "Que souhaitez-vous commander ?",
                            style = TextStyle(
                                fontFamily =  FontFamily(Font(R.font.sofiapro_regular)),
                                fontWeight = FontWeight.Bold, // 700 weight
                                fontSize = 30.sp,             // Font size 30px
                                lineHeight = 30.sp,           // Line height 30px
                                textAlign = TextAlign.Left    // Align text to the left
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f).padding(8.dp)
                        )

                        // Menu button
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar
                    SearchBar(
                        searchText = menuViewModel.searchText,
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
                            item {
                                // si l'utilisateur est connecté en tant que restaurateur, on affiche le bouton de suppression
                                if (idRestaurantOfConnectedUser == menu.restaurantId) {
                                    menuViewModel.setIsConnectedRestorer(true)
                                }
                                else {
                                    menuViewModel.setIsConnectedRestorer(false)
                                }
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
    )
}
