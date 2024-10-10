package com.uds.foufoufood.activities.auth

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uds.foufoufood.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.layout_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_profile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)

        val groupAddressExisting: LinearLayout = findViewById(R.id.group_address_existing)
        val groupNoAddress: LinearLayout = findViewById(R.id.group_no_address)

        // todo -> modifier les valeurs des champs en fonction de l'utilisateur connecté
        // profileName, profileType

        // todo : par défaut bloquer la modification du champ email
        // editTextProfileEmailAddress
        // si clique sur ImageButtonEditEmail -> activer la modification du champ email
        // si clique sur ImageButtonSaveEmail -> sauvegarder la modification du champ email

        if (true) {
            groupAddressExisting.visibility = View.VISIBLE
            groupNoAddress.visibility = View.GONE

            // Afficher l'adresse existante
            val editTextStreetNumber: EditText = findViewById(R.id.editTextProfileStreetNumber)
            val editTextStreetName: EditText = findViewById(R.id.editTextProfileStreetName)
            val editTextPostalCode: EditText = findViewById(R.id.editTextProfilePostalCode)
            val editTextCity: EditText = findViewById(R.id.editTextProfileCity)

            // Supposons que getUserAddress() retourne un objet Address avec les propriétés appropriées

            editTextStreetNumber.setText("123")
            editTextStreetName.setText("Rue de la Paix")
            editTextPostalCode.setText("75000")
            editTextCity.setText("Paris")
        } else {
            groupAddressExisting.visibility = View.GONE
            groupNoAddress.visibility = View.VISIBLE
        }
    }
}