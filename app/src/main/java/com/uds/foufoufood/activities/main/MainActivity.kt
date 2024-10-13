package com.uds.foufoufood.activities.main

import UserRepository
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.uds.foufoufood.factory.AdminViewModelFactory
import com.uds.foufoufood.factory.UserViewModelFactory
import com.uds.foufoufood.navigation.UserNavHost
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.repository.AdminRepository
import com.uds.foufoufood.view.MainScreen
import com.uds.foufoufood.viewmodel.AdminViewModel
import com.uds.foufoufood.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var adminViewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = RetrofitHelper.getRetrofitInstance(this)
        val userApi = retrofit.create(UserApi::class.java)
        val userRepository = UserRepository(userApi)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository, this)).get(
            UserViewModel::class.java)

        val adminRepository = AdminRepository(userApi, this)
        adminViewModel = ViewModelProvider(this, AdminViewModelFactory(adminRepository)).get(
            AdminViewModel::class.java)
        setContent {
            val navController = rememberNavController()
            MainScreen(navController = navController, userViewModel = userViewModel, adminViewModel = adminViewModel)
        }
    }
}