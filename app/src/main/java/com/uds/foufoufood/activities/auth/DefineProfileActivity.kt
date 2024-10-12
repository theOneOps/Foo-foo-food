package com.uds.foufoufood.activities.auth

import UserRepository
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.uds.foufoufood.R
import com.uds.foufoufood.factory.UserViewModelFactory
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.network.UserServiceImpl
import com.uds.foufoufood.ui.page.DefineProfileScreen
import com.uds.foufoufood.viewmodel.UserViewModel

class DefineProfileActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Récupération de l'email
        email = intent.getStringExtra("email") ?: throw IllegalArgumentException("Email is missing")

        // Initialisation du ViewModel
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val userApi = retrofit.create(UserApi::class.java)
        val userService = UserServiceImpl(userApi)
        val userRepository = UserRepository(userService)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        setContent {
            DefineProfile(
                userViewModel = userViewModel,
                email = email,
                onProfileValidated = { navigateToProfileActivity() }
            )
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)
    }

    private fun navigateToProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun DefineProfile(
    userViewModel: UserViewModel,
    email: String,
    onProfileValidated: () -> Unit
) {
    // Observer l'état du succès de l'enregistrement final avec observeAsState
    val registrationCompleteSuccess by userViewModel.registrationCompleteSuccess.observeAsState()

    var selectedProfile by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Réagir au succès de l'enregistrement final
    LaunchedEffect(registrationCompleteSuccess) {
        if (registrationCompleteSuccess == true) {
            onProfileValidated()
        } else if (registrationCompleteSuccess == false) {
            Toast.makeText(context, "Échec de l'inscription", Toast.LENGTH_SHORT).show()
        }
    }

    // Interface de sélection du profil
    DefineProfileScreen(
        onValidateClick = {
            if (selectedProfile.isNullOrEmpty()) {
                Toast.makeText(context, "Veuillez choisir un type de profil", Toast.LENGTH_SHORT).show()
            } else {
                // Appeler le ViewModel pour compléter l'enregistrement
                userViewModel.completeRegistration(email, selectedProfile!!)
            }
        }
    )
}
