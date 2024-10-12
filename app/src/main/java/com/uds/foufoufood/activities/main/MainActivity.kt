package com.uds.foufoufood.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.uds.foufoufood.ui.theme.FouFouFoodTheme
import com.uds.foufoufood.model.User
import com.uds.foufoufood.ui.page.AdminNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FouFouFoodTheme {
                // Utilisation du Scaffold pour structurer l'application
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Appelle le composable MainScreen qui contient la navigation
                    MainScreen()
                }
            }
        }
    }
}
@Composable
fun MainScreen() {
    // Crée le NavController ici
    val navController = rememberNavController()

    // Passe le NavController à AppNavHost ou AdminPage
    AdminNavHost(navController = navController)
}