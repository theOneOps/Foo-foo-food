package com.uds.foufoufood.activities.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
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
        setContentView(R.layout.layout_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)
    }


    /*
    Pour le code de vérification

    val editText1 = findViewById<EditText>(R.id.editTextTextVerificationCode1)
    val editText2 = findViewById<EditText>(R.id.editTextTextVerificationCode2)
    val editText3 = findViewById<EditText>(R.id.editTextTextVerificationCode3)
    val editText4 = findViewById<EditText>(R.id.editTextTextVerificationCode4)
    val editText5 = findViewById<EditText>(R.id.editTextTextVerificationCode5)
    val editText6 = findViewById<EditText>(R.id.editTextTextVerificationCode6)

    val editTexts = listOf(editText1, editText2, editText3, editText4, editText5, editText6)

    for (i in editTexts.indices) {
        editTexts[i].addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Aucune action nécessaire ici
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Vérifie si un caractère a été saisi
                if (s?.length == 1 && i < editTexts.size - 1) {
                    editTexts[i + 1].requestFocus() // Passer au champ suivant
                } else if (s != null) {
                    if (s.isEmpty() && i > 0) {
                        // Si le champ devient vide, passe au champ précédent
                        editTexts[i - 1].requestFocus()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Aucune action nécessaire ici
            }
        })

        // Ajoute un écouteur pour la suppression
        editTexts[i].setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                // Si la case actuelle est vide
                if (editTexts[i].text.isEmpty()) {
                    if (i > 0) {
                        // Supprime le caractère dans la case précédente
                        editTexts[i - 1].setText("") // Supprime le chiffre dans la case précédente
                        editTexts[i - 1].requestFocus() // Passe au champ précédent
                    }
                } else if (i == editTexts.size - 1) {
                    // Si on est dans la dernière case et on supprime un caractère
                    if (editTexts[i].text.length == 1) {
                        editTexts[i].setText("") // Supprime le chiffre
                        editTexts[i].requestFocus() // Reste dans la même case
                    }
                }
                return@setOnKeyListener true // Consomme l'événement
            }
            false // Laisse l'événement continuer si ce n'est pas un cas géré
        }
    }*/

    /*
    Code pour les boutons radio de la page "choix du profil"
    val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
    val textViewResult = findViewById<TextView>(R.id.textViewResult)

    radioGroup.setOnCheckedChangeListener { group, checkedId ->
        when (checkedId) {
            R.id.radioButton1 -> textViewResult.text = getString(R.string.customer_choice)
            R.id.radioButton2 -> textViewResult.text = getString(R.string.owner_choice)
            R.id.radioButton3 -> textViewResult.text = getString(R.string.delivery_choice)
        }
    }*/

    /*
    code pour la page de profile

    val groupAddressExisting: LinearLayout = findViewById(R.id.group_address_existing)
    val groupNoAddress: LinearLayout = findViewById(R.id.group_no_address)

    // Supposons que vous avez une fonction qui vérifie si l'utilisateur a une adresse
    if (userHasAddress()) {
        groupAddressExisting.visibility = View.VISIBLE
        groupNoAddress.visibility = View.GONE

        // Afficher l'adresse existante
        val editTextStreetNumber: EditText = findViewById(R.id.editTextStreetNumber)
        val editTextStreetName: EditText = findViewById(R.id.editTextStreetName)
        val editTextPostalCode: EditText = findViewById(R.id.editTextPostalCode)
        val editTextCity: EditText = findViewById(R.id.editTextCity)

        // Supposons que getUserAddress() retourne un objet Address avec les propriétés appropriées

        editTextStreetNumber.setText("123")
        editTextStreetName.setText("Rue de la Paix")
        editTextPostalCode.setText("75000")
        editTextCity.setText("Paris")
    } else {
        groupAddressExisting.visibility = View.GONE
        groupNoAddress.visibility = View.VISIBLE
    }

    + fonction qui récupère l'adresse de l'utilisateur*/
}