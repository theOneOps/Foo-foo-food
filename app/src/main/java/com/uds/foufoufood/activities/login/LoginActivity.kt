package com.uds.foufoufood.activities.login

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
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
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)


        val dialogView = LayoutInflater.from(this).inflate(R.layout.forgot_password_dialog, null)
        val dialog = Dialog(this, R.style.CustomDialogTheme)
        dialog.setContentView(dialogView)

        val forgotPasswordButton = findViewById<TextView>(R.id.forgot_password_button)
        forgotPasswordButton.setOnClickListener {
            dialog.show()
        }
    }
}