package com.uds.foufoufood.activities.auth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.uds.foufoufood.R
import com.uds.foufoufood.ui.page.ProfileScreen
import com.uds.foufoufood.viewmodel.UserViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Charger les données utilisateur
        userViewModel.getUserProfile()

        // Utilisation de Jetpack Compose pour afficher l'interface utilisateur
        setContent {
            Profile(userViewModel = userViewModel)
        }

        // Appliquer des insets système
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.decorView.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Modifier la couleur de la barre d'état
        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)
    }
}

@Composable
fun Profile(userViewModel: UserViewModel) {
    // Observer les données de l'utilisateur connecté
    val user by userViewModel.user.observeAsState()

    // Si l'utilisateur est non nul, afficher ses informations
    user?.let {
        ProfileScreen(user = it)
    } ?: run {
        Text("Chargement des données de l'utilisateur...")
    }
}