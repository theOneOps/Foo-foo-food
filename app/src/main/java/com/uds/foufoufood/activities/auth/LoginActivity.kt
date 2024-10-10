package com.uds.foufoufood.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uds.foufoufood.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.layout_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)

        val buttonRegister = findViewById<TextView>(R.id.linkToSignUp)
        buttonRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val linkToForgotPassword = findViewById<TextView>(R.id.textViewLoginForgotPassword)
        linkToForgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // todo -> ajouter la gestion de la connexion avec email et mot de passe
        // editTextLoginEmailAddress et editTextLoginPassword
        // buttonLogin

        // todo -> ajouter la gestion de la connexion avec google et facebook
        // layout_login_google_button et layout_login_facebook_button
    }
}