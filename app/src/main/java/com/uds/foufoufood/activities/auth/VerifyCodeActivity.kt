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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.uds.foufoufood.factory.UserViewModelFactory
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.network.UserServiceImpl
import com.uds.foufoufood.ui.page.VerifyCodeScreen
import com.uds.foufoufood.viewmodel.UserViewModel

class VerifyCodeActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Récupération de l'email passé en paramètre
        email = intent.getStringExtra("email") ?: throw IllegalArgumentException("Email is missing")

        // Initialisation du ViewModel
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val userApi = retrofit.create(UserApi::class.java)
        val userService = UserServiceImpl(userApi)
        val userRepository = UserRepository(userService)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        // Utilisation de Compose
        setContent {
            VerifyCode(
                email = email,
                userViewModel = userViewModel,
                onVerifySuccess = {
                    val intent = Intent(this, DefineProfileActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun VerifyCode(
    email: String,
    userViewModel: UserViewModel,
    onVerifySuccess: () -> Unit
) {
    val context = LocalContext.current

    val codeVerificationSuccess by userViewModel.codeVerificationSuccess.observeAsState()

    LaunchedEffect(codeVerificationSuccess) {
        if (codeVerificationSuccess == true) {
            onVerifySuccess()
        } else if (codeVerificationSuccess == false) {
            Toast.makeText(context, "Le code est incorrect", Toast.LENGTH_SHORT).show()
        }
    }

    VerifyCodeScreen(
        onVerifyClick = { enteredCode ->
            if (enteredCode.length == 6) {
                userViewModel.verifyCode(email, enteredCode)
            } else {
                Toast.makeText(context, "Veuillez entrer le code complet", Toast.LENGTH_SHORT).show()
            }
        },
        onResendClick = {
            userViewModel.resendVerificationCode(email)
        }
    )
}
