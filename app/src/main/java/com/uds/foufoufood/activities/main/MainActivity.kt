package com.uds.foufoufood.activities.main

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.uds.foufoufood.R
import com.uds.foufoufood.models.AuthResponse
import com.uds.foufoufood.models.LoginRequest
import com.uds.foufoufood.models.User
import com.uds.foufoufood.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configurer Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Exemple d'inscription d'un utilisateur
        val newUser = User(
            name = "John Doe",
            email = "john@example.com",
            password = "password123",
            isAdmin = false
        )
        registerUser(newUser)

        // Exemple de connexion d'un utilisateur
        val loginRequest = LoginRequest("john@example.com", "password123")
        loginUser(loginRequest)
    }

    // Méthode pour enregistrer un utilisateur
    private fun registerUser(user: User) {
        val call = apiService.registerUser(user)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    Log.d("MainActivity", "Registration Successful: ${authResponse?.user}")
                    Log.d("MainActivity", "Token: ${authResponse?.token}")
                } else {
                    Log.e("MainActivity", "Registration Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("MainActivity", "API Call Failed", t)
            }
        })
    }

    // Méthode pour se connecter
    private fun loginUser(loginRequest: LoginRequest) {
        val call = apiService.loginUser(loginRequest)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    Log.d("MainActivity", "Login Successful: ${authResponse?.user}")
                    Log.d("MainActivity", "Token: ${authResponse?.token}")
                } else {
                    Log.e("MainActivity", "Login Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("MainActivity", "API Call Failed", t)
            }
        })
    }
}