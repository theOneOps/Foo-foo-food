package com.uds.foufoufood.activities.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.uds.foufoufood.activities.auth.LoginActivity
import com.uds.foufoufood.activities.auth.RegisterFirstPartActivity
import com.uds.foufoufood.ui.page.WelcomeScreen


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WelcomeScreen(
                onNavigateToLogin = {
                    // Navigate to Login screen
                    startActivity(Intent(this, LoginActivity::class.java))
                },
                onNavigateToRegister = {
                    // Navigate to Register screen
                    startActivity(Intent(this, RegisterFirstPartActivity::class.java))
                }
            )
        }
        // todo -> gestion connexion fcb et google
    }
}