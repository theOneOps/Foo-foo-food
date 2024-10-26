package com.uds.foufoufood.activities.main

import UserRepository
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.uds.foufoufood.network.MenuApi
import com.uds.foufoufood.network.OrderApi
import com.uds.foufoufood.network.RestaurantApi
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.repository.AdminRepository
import com.uds.foufoufood.repository.DeliveryRepository
import com.uds.foufoufood.repository.MenuRepository
import com.uds.foufoufood.repository.OrderRepository
import com.uds.foufoufood.repository.RestaurantRepository
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

        // ADMIN
        val adminRepository = AdminRepository(userApi, this)
        adminUsersViewModel = AdminUsersViewModel(adminRepository)
        adminRestaurantsViewModel = AdminRestaurantsViewModel(restaurantRepository)

        // DELIVERY
        val deliveryRepository = DeliveryRepository(userApi, this)
        deliveryViewModel = DeliveryViewModel(deliveryRepository, userRepository, this)
        val orderApi = retrofit.create(OrderApi::class.java)
        val orderRepository = OrderRepository(orderApi, this)
        orderViewModel = OrderViewModel(orderRepository, this)

        homeViewModel = HomeViewModel(restaurantRepository)

        val menuApi = retrofit.create(MenuApi::class.java)
        val menuRepository = MenuRepository(menuApi)
        menuViewModel = MenuViewModel(menuRepository)

        setContent {
            val navController = rememberNavController()

            /*UnifiedNavHost(
                navController = navController,
                connectUser = connectUser ?: "",
                userViewModel = userViewModel,
                adminUsersViewModel = adminUsersViewModel,
                adminRestaurantsViewModel = adminRestaurantsViewModel,
                deliveryViewModel = deliveryViewModel,
                orderViewModel = orderViewModel,
                homeViewModel = homeViewModel,
                menuViewModel = menuViewModel
            )*/
            MainScreen(
                navController = navController,
                userViewModel = userViewModel,
                adminUsersViewModel = adminUsersViewModel,
                adminRestaurantsViewModel = adminRestaurantsViewModel,
                deliveryViewModel = deliveryViewModel,
                orderViewModel = orderViewModel,
                homeViewModel = homeViewModel,
                menuViewModel = menuViewModel
            )

        }
    }
}
