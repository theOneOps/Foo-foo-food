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
import com.uds.foufoufood.ui.page.RegisterFirstPartScreen
import com.uds.foufoufood.viewmodel.UserViewModel

class RegisterFirstPartActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)

        // Initialisation du ViewModel
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val userApi = retrofit.create(UserApi::class.java)
        val userService = UserServiceImpl(userApi)
        val userRepository = UserRepository(userService)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        setContent {
            RegisterFirstPart(
                userViewModel = userViewModel,
                onNavigateToLogin = {
                    startActivity(Intent(this, LoginActivity::class.java))
                },
                onNavigateToVerifyCode = { email ->
                    val intent = Intent(this, VerifyCodeActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun RegisterFirstPart(
    userViewModel: UserViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToVerifyCode: (String) -> Unit
) {
    val context = LocalContext.current

    // Champs d'entrée utilisateur (nom, email, mot de passe)
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observer LiveData registrationInitSuccess avec observeAsState
    val registrationSuccess by userViewModel.registrationInitSuccess.observeAsState()

    // Réagir au changement d'état de registrationSuccess
    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess == true) {
            onNavigateToVerifyCode(email)
        }
    }

    // Affichage de l'UI
    RegisterFirstPartScreen(
        name = name,
        onNameChange = { name = it },
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        onRegisterClick = {
            if (isValidName(name) && isValidEmail(email) && isValidPassword(password)) {
                userViewModel.initiateRegistration(name, email, password)
            } else {
                Toast.makeText(context, "Veuillez entrer des informations valides", Toast.LENGTH_SHORT).show()
            }
        },
        onNavigateToLogin = onNavigateToLogin
    )
}

fun isValidName(name: String): Boolean {
    return name.isNotEmpty()
}