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
import com.uds.foufoufood.databinding.LayoutRegisterBinding
import com.uds.foufoufood.factory.UserViewModelFactory
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.network.UserServiceImpl
import com.uds.foufoufood.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: LayoutRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = LayoutRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainRegister) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)

        // Initialisation du ViewModel
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val userApi = retrofit.create(UserApi::class.java)
        val userService = UserServiceImpl(userApi)
        val userRepository = UserRepository(userService)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        binding.linkToSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextRegisterName.text.toString()
            val email = binding.editTextRegisterEmail.text.toString()
            val password = binding.editTextRegisterPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                // Envoie des informations à la première étape du processus de vérification
                Log.d("RegisterActivity", "Name: $name, Email: $email, Password: $password")
                userViewModel.initiateRegistration(name, email, password)
            }
        }

        // Observer l'événement de succès de la première étape
        userViewModel.registrationInitSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                // Redirection vers VerifyCodeActivity pour entrer le code de vérification
                val intent = Intent(this, VerifyCodeActivity::class.java)
                intent.putExtra("email", binding.editTextRegisterEmail.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Échec de l'inscription", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

