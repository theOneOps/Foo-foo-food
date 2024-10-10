package com.uds.foufoufood.activities.auth

import UserRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uds.foufoufood.R
import com.uds.foufoufood.databinding.LayoutLoginBinding
import com.uds.foufoufood.factory.UserViewModelFactory
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.network.UserServiceImpl
import com.uds.foufoufood.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository
    private lateinit var binding: LayoutLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = LayoutLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLogin) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)

        val retrofit = RetrofitHelper.getRetrofitInstance()
        val userApi = retrofit.create(UserApi::class.java)
        val userService = UserServiceImpl(userApi)
        userRepository = UserRepository(userService)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        binding.linkToSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.textViewLoginForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextLoginEmailAddress.text.toString()
            val password = binding.editTextLoginPassword.text.toString()
            Log.d("LoginActivity", "Email: $email, Password: $password")

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("LoginActivity", "Logging in...")
                userViewModel.login(email, password)
            }
        }

        // Observation des changements dans le ViewModel
        userViewModel.user.observe(this, Observer { user ->
            if (user != null) {
                // Rediriger vers la page d'accueil ou afficher un message de succès
                Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
            } else {
                // Afficher un message d'erreur
                Toast.makeText(this, "Échec de la connexion", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
