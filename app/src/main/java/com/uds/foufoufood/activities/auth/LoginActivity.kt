package com.uds.foufoufood.activities.auth

import UserRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.uds.foufoufood.R
import com.uds.foufoufood.factory.UserViewModelFactory
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.network.UserServiceImpl
import com.uds.foufoufood.ui.page.LoginScreen
import com.uds.foufoufood.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val retrofit = RetrofitHelper.getRetrofitInstance()
        val userApi = retrofit.create(UserApi::class.java)
        val userService = UserServiceImpl(userApi)
        userRepository = UserRepository(userService)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)

        setContent {
            Login(
                userViewModel = userViewModel,
                onNavigateToRegister = {
                    startActivity(Intent(this, RegisterFirstPartActivity::class.java))
                },
                onLoginSuccess = {
                    // TODO: Rediriger vers l'écran d'accueil
                    // Par exemple :
                    // startActivity(Intent(this, HomeActivity::class.java))
                    // finish()
                    Log.d("LoginActivity", "Login success")
                }
            )
        }
    }
}

@Composable
fun Login(
    userViewModel: UserViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val user by userViewModel.user.observeAsState()
    Log.d("LoginActivity", "User: $user")

    val errorMessage by userViewModel.errorMessage.observeAsState()

    LaunchedEffect(user) {
        user?.let {
            Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show()
            onLoginSuccess()
        }
    }

    Column {
        // Affichage de l'interface de connexion
        LoginScreen(
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            onNavigateToRegister = onNavigateToRegister,
            onLoginClick = {
                if (isValidEmail(email) && isValidPassword(password)) {
                    userViewModel.login(email, password)
                } else {
                    Toast.makeText(context, "Veuillez entrer des informations valides", Toast.LENGTH_SHORT).show()
                }
            }
        )

        // Si l'utilisateur est non nul, afficher ses informations
        user?.let { currentUser ->
            Text(text = "Nom: ${currentUser.name}")
            Text(text = "Email: ${currentUser.email}")
        }

        // Afficher un message d'erreur s'il y en a un
        errorMessage?.let { error ->
            Text(text = "Erreur: $error", color = Color.Red)
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return email.contains("@") && email.contains(".")
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 6
}
