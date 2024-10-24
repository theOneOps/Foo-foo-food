package com.uds.foufoufood.view

import android.util.Log
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.res.fontResource
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
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.Category
import com.uds.foufoufood.data_class.model.Menu
import com.uds.foufoufood.data_class.model.Restaurant
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.ui.component.CategoryPills
import com.uds.foufoufood.ui.component.RestaurantCard
import com.uds.foufoufood.ui.component.SearchBar
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.UserViewModel
import kotlinx.coroutines.launch

val restaurantTest = Restaurant(
    userId = "1",
    restaurantId = "1",
    name = "Le Gourmet",
    address = Address(123, "Rue de Paris", "75000", "Paris", "France"),
    speciality = "French cuisine",
    phone = "0123456789",
    openingHours = "09:00 - 22:00",
    items = listOf(
        Menu(
            _id = "1",
            name = "Coq au Vin",
            description = "Classic French chicken dish cooked with wine",
            price = 25.0,
            category = "Main course",
            image = "https://images.unsplash.com/photo-1468070975228-085c1fdd2d3e?q=80&w=1973&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            restaurantId = "1"
        ), Menu(
            _id = "2",
            name = "Crème Brûlée",
            description = "Rich custard base topped with a layer of caramelized sugar",
            price = 10.0,
            category = "Dessert",
            image = "https://images.unsplash.com/photo-1487004121828-9fa15a215a7a?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            restaurantId = "1"
        )
    ),
    rating = 4.7,
    reviews = listOf("Delicious food!", "Amazing ambiance."),
    imageUrl = "https://images.unsplash.com/photo-1498654896293-37aacf113fd9?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
)

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    userViewModel: UserViewModel,
    menuViewModel: MenuViewModel
) {
    Log.d("HomeScreen", "HomeScreen")
    // State for controlling drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Initialize the categories and restaurants here with drawables
    LaunchedEffect(Unit) {
        homeViewModel.initialize(
            categoriesList = listOf(
                Category(1, "Pizza", R.drawable.ic_category_pizza),
                Category(2, "Burger", R.drawable.ic_category_burger),
                Category(3, "Mexican", R.drawable.ic_category_mexican),
                Category(4, "Pizza", R.drawable.ic_category_pizza),
                Category(5, "Burger", R.drawable.ic_category_burger),
                Category(6, "Pizza", R.drawable.ic_category_pizza),
                Category(7, "Burger", R.drawable.ic_category_burger),

                ),
            restaurantsList = listOf(restaurantTest)
        )
    }

    // Use the default SansSerif font family
    val defaultFontFamily = FontFamily.SansSerif

    // Modal navigation drawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController, closeDrawer = {
                scope.launch { drawerState.close() }
            },
            logout = userViewModel::logout, userViewModel = userViewModel)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    searchText = homeViewModel.searchText,
                    onSearchTextChanged = homeViewModel::onSearchQueryChanged
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Pills and Restaurant List
                LazyColumn {
                    // Category Pills
                    item {
                        CategoryPills(
                            categories = homeViewModel.categories,
                            selectedCategory = homeViewModel.selectedCategory,
                            onCategorySelected = homeViewModel::onCategorySelected
                        )
                    }

                    // Restaurant List
                    items(homeViewModel.filteredRestaurants) { restaurant ->
                        RestaurantCard(navController, menuViewModel, restaurant)
                    }
                }
            }
        }
    )
}

@Composable
fun DrawerContent(navController: NavHostController, closeDrawer: () -> Unit, logout: () -> Unit, userViewModel: UserViewModel) {
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
                .padding(bottom = 20.dp),
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

        // Menu items with icons
        DrawerMenuItem(
            icon = ImageVector.vectorResource(R.drawable.ic_drawer_orders),
            label = stringResource(R.string.my_orders),
            onClick = {
                closeDrawer()
                // Handle navigation to orders
            },
        )

        DrawerMenuItem(
            icon = ImageVector.vectorResource(R.drawable.ic_profile_avatar),
            label = stringResource(R.string.my_profile),
            onClick = {
                closeDrawer()
                // Handle navigation to profile
                navController.navigate(Screen.Profile.route)
            }
        )

        DrawerMenuItem(
            icon = ImageVector.vectorResource(R.drawable.ic_drawer_address),
            label = stringResource(R.string.delivery_address),
            onClick = {
                closeDrawer()
                // Handle navigation to address
                navController.navigate(Screen.Address.route)
            },
        )

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

//        // Category Pills
//        CategoryPills(
//            categories = homeViewModel.categories,
//            selectedCategory = homeViewModel.selectedCategory,
//            onCategorySelected = homeViewModel::onCategorySelected
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Restaurant List
//        RestaurantList(restaurants = homeViewModel.filteredRestaurants)
