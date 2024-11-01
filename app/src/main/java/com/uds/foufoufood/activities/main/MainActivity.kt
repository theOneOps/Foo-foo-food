package com.uds.foufoufood.activities.main

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.uds.foufoufood.R
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
import com.uds.foufoufood.repository.UserRepository
import com.uds.foufoufood.view.MainScreen
import com.uds.foufoufood.viewmodel.AdminRestaurantsViewModel
import com.uds.foufoufood.viewmodel.AdminUsersViewModel
import com.uds.foufoufood.viewmodel.CartViewModel
import com.uds.foufoufood.viewmodel.DeliveryViewModel
import com.uds.foufoufood.viewmodel.MenuViewModel
import com.uds.foufoufood.viewmodel.OrderViewModel
import com.uds.foufoufood.viewmodel.RestaurantViewModel
import com.uds.foufoufood.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var adminUsersViewModel: AdminUsersViewModel
    private lateinit var adminRestaurantsViewModel: AdminRestaurantsViewModel
    private lateinit var deliveryViewModel: DeliveryViewModel
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
        })

        createNotificationChannels()

        val retrofit = RetrofitHelper.getRetrofitInstance(this)
        requestNotificationPermission(this)

        // Initialize repositories and api
        val userApi = retrofit.create(UserApi::class.java)
        val userRepository = UserRepository(userApi, this)

        val restaurantApi = retrofit.create(RestaurantApi::class.java)
        val restaurantRepository = RestaurantRepository(restaurantApi)

        val orderApi = retrofit.create(OrderApi::class.java)
        val orderRepository = OrderRepository(orderApi)

        val menuApi = retrofit.create(MenuApi::class.java)
        val menuRepository = MenuRepository(menuApi)

        val adminRepository = AdminRepository(userApi, this)

        // USER
        userViewModel = UserViewModel(userRepository, this)

        // ADMIN
        adminUsersViewModel = AdminUsersViewModel(adminRepository)
        adminRestaurantsViewModel = AdminRestaurantsViewModel(restaurantRepository)

        // DELIVERY
        val deliveryRepository = DeliveryRepository(userApi)
        deliveryViewModel = DeliveryViewModel(deliveryRepository, userRepository, this)

        orderViewModel = OrderViewModel(orderRepository)
        cartViewModel = CartViewModel(orderRepository, userViewModel)

        menuViewModel = MenuViewModel(menuRepository)

        restaurantViewModel = RestaurantViewModel(restaurantRepository)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        userViewModel.googleSignInClient = googleSignInClient
        userViewModel.auth = auth

        setContent {
            val navController = rememberNavController()

            MainScreen(
                navController = navController,
                userViewModel = userViewModel,
                adminUsersViewModel = adminUsersViewModel,
                adminRestaurantsViewModel = adminRestaurantsViewModel,
                deliveryViewModel = deliveryViewModel,
                orderViewModel = orderViewModel,
                menuViewModel = menuViewModel,
                restaurantViewModel = restaurantViewModel,
                cartViewModel = cartViewModel,
                orderRepository = orderRepository,
                googleSignInClient = googleSignInClient,
                auth = auth
            )

        }
    }

    // Check if the app has the permission to show notifications
    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted; you can now show notifications
            } else {
                // Permission denied; handle accordingly
            }
        }
    }

    // Request the permission to show notifications
    private fun requestNotificationPermission(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Create notification channels for Android 8.0 and above
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            // Channel for client order notifications
            val clientOrderChannel = NotificationChannel(
                "client_order_notifications",
                "Client Order Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for client order updates"
            }
            notificationManager.createNotificationChannel(clientOrderChannel)

            // Channel for delivery order notifications
            val deliveryOrderChannel = NotificationChannel(
                "order_notifications", "Order Notifications", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for delivery orders assigned to drivers"
            }
            notificationManager.createNotificationChannel(deliveryOrderChannel)
        }
    }
}
