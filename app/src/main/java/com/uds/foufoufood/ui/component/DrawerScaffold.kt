package com.uds.foufoufood.ui.component

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.uds.foufoufood.R
import com.uds.foufoufood.navigation.Screen
import com.uds.foufoufood.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun DrawerScaffold(
    navController: NavHostController,
    userViewModel: UserViewModel,
    currentScreen: String,
    screenContent: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        DrawerContent(
            navController = navController,
            closeDrawer = { scope.launch { drawerState.close() } },
            logout = userViewModel::logout,
            userViewModel = userViewModel,
            currentScreen = currentScreen
        )
    }, content = {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Main content displayed in the background
            screenContent()

            // Menu button overlayed at the top-right corner
            IconButton(
                onClick = {
                    scope.launch { drawerState.open() }
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp, top = 20.dp)
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }

            Image(
                painter = painterResource(R.drawable.logo_only),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(85.dp)
                    .clip(CircleShape)
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            )
        }
    })
}


@Composable
fun DrawerContent(
    navController: NavHostController,
    closeDrawer: () -> Unit,
    logout: () -> Unit,
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
            Icon(painter = painterResource(R.drawable.ic_profile_avatar),
                contentDescription = "Edit Profile",
                tint = colorResource(R.color.orange), // Orange color
                modifier = Modifier
                    .size(64.dp)
                    .padding(start = 8.dp)
                    .clickable {
                        closeDrawer()
                        navController.navigate(Screen.Profile.route)
                    })

            Spacer(modifier = Modifier.width(16.dp))

            // User information
            Column {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    fontFamily = FontFamily(Font(R.font.sofiapro_bold))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = email,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.sofiapro_medium))
                )
            }
        }

        if (currentScreen !in listOf(
                Screen.AdminClient.route,
                Screen.AdminLivreur.route,
                Screen.AdminGerant.route,
                Screen.AdminRestaurant.route,
                Screen.DeliveryOrderDetailsPage.route,
                Screen.DeliveryAvailablePage
            )
        ) {
            if (userViewModel.user.value?.role != "admin" && userViewModel.user.value?.role != "livreur") {
                DrawerMenuItem(
                    icon = ImageVector.vectorResource(R.drawable.home),
                    label = stringResource(R.string.home),
                    onClick = {
                        closeDrawer()
                        navController.navigate(Screen.HomeRestaurant.route)
                    },
                )
            }
        }
        if (userViewModel.user.value?.role == "client") DrawerMenuItem(
            icon = ImageVector.vectorResource(R.drawable.ic_drawer_orders),
            label = stringResource(R.string.my_orders),
            onClick = {
                closeDrawer()
                navController.navigate(Screen.OrderTracking.route)
            },
        )

        if (currentScreen in listOf(
                Screen.DeliveryOrderDetailsPage.route,
                Screen.DeliveryAvailablePage.route,
                Screen.Profile.route,
            )
        ) {
            if (userViewModel.user.value?.role == "livreur") {
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
        }

        DrawerMenuItem(icon = ImageVector.vectorResource(R.drawable.ic_profile_avatar),
            label = stringResource(R.string.my_profile),
            onClick = {
                closeDrawer()
                // Handle navigation to profile
                navController.navigate(Screen.Profile.route)
            })

        if (currentScreen !in listOf(
                Screen.AdminClient.route,
                Screen.AdminLivreur.route,
                Screen.AdminGerant.route,
                Screen.AdminRestaurant.route,
                Screen.DeliveryOrderDetailsPage.route,
                Screen.DeliveryAvailablePage
            )
        ) {
            if (userViewModel.user.value?.role != "admin" && userViewModel.user.value?.role != "livreur") {
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
        }

        Spacer(modifier = Modifier.weight(1f))

        // butuon deconnexion centré
        Button(
            onClick = {
                closeDrawer()
                logout()
                userViewModel.auth.signOut()
                userViewModel.googleSignInClient.signOut()
                Toast.makeText(context, R.string.logout_success, Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Welcome.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.orange)), // Orange color
            shape = RoundedCornerShape(36.dp),
            modifier = Modifier
                .fillMaxWidth()
                //.height(50.dp)
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_drawer_logout),
                contentDescription = stringResource(R.string.logout),
                tint = colorResource(R.color.white),
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                stringResource(R.string.logout),
                color = colorResource(R.color.white),
                fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
                fontSize = 18.sp
            )
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
            tint = colorResource(R.color.orange) // Orange color
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Menu item label
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.sofiapro_medium)),
        )
    }
}
