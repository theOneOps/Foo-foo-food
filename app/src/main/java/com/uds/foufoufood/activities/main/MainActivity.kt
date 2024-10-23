package com.uds.foufoufood.activities.main

import UserRepository
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.uds.foufoufood.network.RestaurantApi
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.repository.AdminRepository
import com.uds.foufoufood.repository.RestaurantRepository
import com.uds.foufoufood.view.MainScreen
import com.uds.foufoufood.viewmodel.AdminViewModel
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var adminViewModel: AdminViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = RetrofitHelper.getRetrofitInstance(this)
        val userApi = retrofit.create(UserApi::class.java)
        val userRepository = UserRepository(userApi)

        val restaurantApi = retrofit.create(RestaurantApi::class.java)
        val restaurantRepository = RestaurantRepository(restaurantApi)

        userViewModel = UserViewModel(userRepository, this)

        val adminRepository = AdminRepository(userApi, this)

        adminViewModel = AdminViewModel(adminRepository)

        homeViewModel = HomeViewModel(restaurantRepository)

        setContent {
            val navController = rememberNavController()
            MainScreen(
                navController = navController,
                userViewModel = userViewModel,
                adminViewModel = adminViewModel,
                homeViewModel = homeViewModel,
            )
        }
    }
}