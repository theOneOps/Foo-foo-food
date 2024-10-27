package com.uds.foufoufood.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import com.uds.foufoufood.ui.component.RestaurantCard
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.ui.component.SpecialityPills
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.RestaurantViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreenRestaurant(
    navController: NavHostController,
    restaurantViewModel: RestaurantViewModel,
    userViewModel: UserViewModel,
    menuViewModel: MenuViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar
                    SearchBar(
                        searchText = restaurantViewModel.searchText,
                        onSearchTextChanged = restaurantViewModel::onSearchQueryChangedSpeciality
                    )

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
}


@Composable
fun BottomNavbarHome(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        // Liste des éléments avec des icônes Material et des libellés
        val items = listOf(
            Pair("Restaurants", R.drawable.storefront),
            Pair("Menus", R.drawable.menubook)
        )

        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index

            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = item.second),
                        contentDescription = item.first,
                        modifier = Modifier.size(if (isSelected) 28.dp else 24.dp),
                        tint = if (isSelected) colorResource(id = R.color.orange) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                selected = isSelected,
                onClick = { onItemSelected(index) },
                label = {
                    Text(
                        text = item.first,
                        fontSize = 12.sp,
                        color = if (isSelected) colorResource(id = R.color.orange) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(id = R.color.orange),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    selectedTextColor = colorResource(id = R.color.orange),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
fun DrawerContent(navController: NavHostController,
                  closeDrawer: () -> Unit, logout: () -> Unit,
                  userViewModel: UserViewModel,
                  currentScreen: String
) {
    val context = LocalContext.current

    val name = userViewModel.user.value?.name ?: ""
    val email = userViewModel.user.value?.email ?: ""

    // Get screen width for 80% drawer width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screenWidth * 0.8f

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(drawerWidth)
            .background(colorResource(R.color.white))
            .padding(12.dp)
    ) {
        // Profile Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar image
            Image(
                painter = painterResource(R.drawable.ic_profile_avatar), // Replace with actual avatar drawable
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // User information
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = email, fontSize = 14.sp, color = Color.Gray)
            }
        }

        if (currentScreen !in listOf(Screen.AdminClient.route, Screen.AdminLivreur.route, Screen.AdminGerant.route, Screen.AdminRestaurant.route, Screen.DeliveryOrderDetailsPage.route, Screen.DeliveryAvailablePage)) {
            DrawerMenuItem(
                icon = ImageVector.vectorResource(R.drawable.home),
                label = stringResource(R.string.home),
                onClick = {
                    closeDrawer()
                    navController.navigate(Screen.HomeRestaurant.route)
                },
            )
        }

        // Menu items with icons
        if (currentScreen !in listOf(Screen.AdminClient.route, Screen.AdminLivreur.route, Screen.AdminGerant.route, Screen.AdminRestaurant.route, Screen.DeliveryOrderDetailsPage.route, Screen.DeliveryAvailablePage)) {
            DrawerMenuItem(
                icon = ImageVector.vectorResource(R.drawable.ic_drawer_orders),
                label = stringResource(R.string.my_orders),
                onClick = {
                    closeDrawer()
                    // Handle navigation to orders
                },
            )
        }

        if (currentScreen in listOf(Screen.DeliveryOrderDetailsPage.route, Screen.DeliveryAvailablePage)) {
            DrawerMenuItem(
                icon = ImageVector.vectorResource(R.drawable.ic_drawer_orders),
                label = stringResource(R.string.my_deliveries),
                onClick = {
                    closeDrawer()
                    // Handle navigation to orders
                    navController.navigate(Screen.DeliveryAllOrdersPage.route)
                },
            )
        }

        DrawerMenuItem(
            icon = ImageVector.vectorResource(R.drawable.ic_profile_avatar),
            label = stringResource(R.string.my_profile),
            onClick = {
                closeDrawer()
                // Handle navigation to profile
                navController.navigate(Screen.Profile.route)
            }
        )

        if (currentScreen !in listOf(Screen.AdminClient.route, Screen.AdminLivreur.route, Screen.AdminGerant.route, Screen.AdminRestaurant.route, Screen.DeliveryOrderDetailsPage.route, Screen.DeliveryAvailablePage)) {
            DrawerMenuItem(
                icon = ImageVector.vectorResource(R.drawable.ic_drawer_address),
                label = stringResource(R.string.delivery_address),
                onClick = {
                    closeDrawer()
                    // Handle navigation to address
                    navController.navigate(Screen.Address.route)
                },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Log out button at the bottom
        Button(
            onClick = {
                closeDrawer()
                logout()
                Toast.makeText(context, R.string.logout_success, Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Welcome.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.orange)), // Orange color
            shape = RoundedCornerShape(36.dp),
            modifier = Modifier
                .fillMaxWidth()
                //.height(50.dp)
                .padding(16.dp)
                .align(Alignment.Start)
        ) {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_drawer_logout),
                contentDescription = stringResource(R.string.logout),
                tint = colorResource(R.color.white),
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.logout), color = colorResource(R.color.white))
        }
    }
}

@Composable
fun DrawerMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier
                .size(36.dp)
                .padding(4.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Menu item label
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
