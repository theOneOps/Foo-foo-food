package com.uds.foufoufood.activities.main

import UserRepository
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.uds.foufoufood.factory.UserViewModelFactory
import com.uds.foufoufood.navigation.AppNavHost
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = RetrofitHelper.getRetrofitInstance()
        val userApi = retrofit.create(UserApi::class.java)
        val userRepository = UserRepository(userApi)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(
            UserViewModel::class.java)

        setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController, userViewModel = userViewModel)
        }
    }
}