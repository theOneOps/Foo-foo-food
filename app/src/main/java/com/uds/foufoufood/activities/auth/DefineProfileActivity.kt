package com.uds.foufoufood.activities.auth

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uds.foufoufood.R

class DefineProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.layout_define_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_define_profile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupTypeProfile)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonCustomer -> textViewResult.text = getString(R.string.customer_choice)
                R.id.radioButtonOwner -> textViewResult.text = getString(R.string.owner_choice)
                R.id.radioButtonDeliveryMan -> textViewResult.text = getString(R.string.delivery_choice)
            }
        }

        // todo : ajouter un bouton pour valider le choix du profil et rediriger vers la page d'accueil adaptée
    }
}