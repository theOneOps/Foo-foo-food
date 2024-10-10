package com.uds.foufoufood.activities.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uds.foufoufood.activities.auth.LoginActivity
import com.uds.foufoufood.activities.auth.RegisterActivity
import com.uds.foufoufood.databinding.LayoutWelcomeBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: LayoutWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = LayoutWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.welcomeLinkToSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonWelcomeStartWithEmail.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // todo -> gestion connexion avec Google et Facebook
    }
}