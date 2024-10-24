package com.uds.foufoufood.activities.main

import UserRepository
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.navigation.AdminNavHost
import com.uds.foufoufood.navigation.AuthNavHost
import com.uds.foufoufood.navigation.ConnectedNavHost
import com.uds.foufoufood.navigation.DeliveryNavHost
import com.uds.foufoufood.network.MenuApi
import com.uds.foufoufood.network.RestaurantApi
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.repository.AdminRepository
import com.uds.foufoufood.repository.DeliveryRepository
import com.uds.foufoufood.repository.OrderRepository
import com.uds.foufoufood.repository.MenuRepository
import com.uds.foufoufood.repository.RestaurantRepository
import com.uds.foufoufood.view.LoadingScreen
import com.uds.foufoufood.view.MainScreen
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var adminUsersViewModel: AdminUsersViewModel
    private lateinit var adminRestaurantsViewModel: AdminRestaurantsViewModel
    private lateinit var deliveryViewModel: DeliveryViewModel
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var menuViewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = RetrofitHelper.getRetrofitInstance(this)
        val userApi = retrofit.create(UserApi::class.java)
        val userRepository = UserRepository(userApi, this)

        val restaurantApi = retrofit.create(RestaurantApi::class.java)
        val restaurantRepository = RestaurantRepository(restaurantApi)

        userViewModel = UserViewModel(userRepository, this)

        //ADMIN
        val adminRepository = AdminRepository(userApi, this)
        adminUsersViewModel = AdminUsersViewModel(adminRepository)
        adminRestaurantsViewModel = AdminRestaurantsViewModel()

        //DELIVERY
        val deliveryRepository = DeliveryRepository(userApi, this)
        deliveryViewModel = DeliveryViewModel(deliveryRepository, userRepository, this)
        val orderRepository = OrderRepository(userApi, this)
        orderViewModel = OrderViewModel(orderRepository, this)

        homeViewModel = HomeViewModel(restaurantRepository)

        val menuApi = retrofit.create(MenuApi::class.java)

        val menuRepository = MenuRepository(menuApi)

        menuViewModel = MenuViewModel(menuRepository)

        setContent {
            val context = this
            val navController = rememberNavController()

            val user by userViewModel.user.observeAsState()
            val isLoading by userViewModel.loading.observeAsState()
            var connectUser by remember { mutableStateOf<String?>("") }

            LaunchedEffect(user) {
                val token = getToken(context)
                if (token != null) {
                    userViewModel.getUserFromToken(token)
                }

                user?.let {
                    connectUser = it.role
                }
            }

            if (isLoading == true) {
                LoadingScreen()
            } else {
                if (user == null) {
                    AuthNavHost(
                        navController = navController,
                        userViewModel = userViewModel
                    )
                } else {
                    MainScreen(
                        navController = navController,
                        userViewModel = userViewModel,
                        adminUsersViewModel = adminUsersViewModel,
                        adminRestaurantsViewModel = adminRestaurantsViewModel,
                        deliveryViewModel = deliveryViewModel,
                        orderViewModel = orderViewModel,
                        homeViewModel = homeViewModel,
                        menuViewModel = menuViewModel,
                        connectUser = connectUser

                    )
                }
            }

        }
    }
}