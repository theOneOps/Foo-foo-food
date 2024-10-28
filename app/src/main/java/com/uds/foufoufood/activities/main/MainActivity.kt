package com.uds.foufoufood.activities.main

import UserRepository
import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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
import com.uds.foufoufood.viewmodel.CartViewModel
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.HomeViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.OrderTrackingViewModel
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
    private lateinit var cartViewModel: CartViewModel
    //private lateinit var orderTrackingViewModel: OrderTrackingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token
            Log.d("FBToken", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        val retrofit = RetrofitHelper.getRetrofitInstance(this)
        val userApi = retrofit.create(UserApi::class.java)
        val userRepository = UserRepository(userApi, this)

        val restaurantApi = retrofit.create(RestaurantApi::class.java)
        val restaurantRepository = RestaurantRepository(restaurantApi)

        userViewModel = UserViewModel(userRepository, this)

        // ADMIN
        val adminRepository = AdminRepository(userApi, this)
        adminUsersViewModel = AdminUsersViewModel(adminRepository)
        adminRestaurantsViewModel = AdminRestaurantsViewModel()

        // DELIVERY
        val deliveryRepository = DeliveryRepository(userApi, this)
        deliveryViewModel = DeliveryViewModel(deliveryRepository, userRepository, this)
        val orderApi = retrofit.create(OrderApi::class.java)
        val orderRepository = OrderRepository(orderApi, this)
        orderViewModel = OrderViewModel(orderRepository, this)
        cartViewModel = CartViewModel(orderRepository, userViewModel)
        //orderTrackingViewModel = OrderTrackingViewModel(orderRepository, userViewModel)

        homeViewModel = HomeViewModel(restaurantRepository)

        val menuApi = retrofit.create(MenuApi::class.java)
        val menuRepository = MenuRepository(menuApi)
        menuViewModel = MenuViewModel(menuRepository)

        setContent {
            val navController = rememberNavController()

            MainScreen(
                navController = navController,
                userViewModel = userViewModel,
                adminUsersViewModel = adminUsersViewModel,
                adminRestaurantsViewModel = adminRestaurantsViewModel,
                deliveryViewModel = deliveryViewModel,
                orderViewModel = orderViewModel,
                homeViewModel = homeViewModel,
                menuViewModel = menuViewModel,
                cartViewModel = cartViewModel,
                orderRepository = orderRepository
            )

        }
    }

    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted; you can now show notifications
            } else {
                // Permission denied; handle accordingly
            }
        }
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1001
    }
}
