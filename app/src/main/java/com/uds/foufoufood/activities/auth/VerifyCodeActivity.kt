package com.uds.foufoufood.activities.auth

import UserRepository
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uds.foufoufood.R
import com.uds.foufoufood.databinding.LayoutVerifyCodeBinding
import com.uds.foufoufood.factory.UserViewModelFactory
import com.uds.foufoufood.models.User
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.network.UserServiceImpl
import com.uds.foufoufood.viewmodel.UserViewModel

class VerifyCodeActivity : AppCompatActivity() {

    private lateinit var binding: LayoutVerifyCodeBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = LayoutVerifyCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)

        // Récupération de l'email passé en paramètre
        email = intent.getStringExtra("email") ?: throw IllegalArgumentException("Email is missing")

        // Initialisation du ViewModel
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val userApi = retrofit.create(UserApi::class.java)
        val userService = UserServiceImpl(userApi)
        val userRepository = UserRepository(userService)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        val editTexts = listOf(
            binding.editTextVerificationCode1,
            binding.editTextVerificationCode2,
            binding.editTextVerificationCode3,
            binding.editTextVerificationCode4,
            binding.editTextVerificationCode5,
            binding.editTextVerificationCode6
        )

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Pas nécessaire
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        // Aller au champ suivant
                        editTexts[i + 1].requestFocus()
                    } else if (s.isNullOrEmpty() && i > 0) {
                        // Retourner au champ précédent si vide
                        editTexts[i - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // Pas nécessaire
                }
            })

            // Gestion de la suppression avec la touche Retour arrière
            editTexts[i].setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (editTexts[i].text.isEmpty() && i > 0) {
                        // Supprimer dans le champ précédent
                        editTexts[i - 1].setText("")
                        editTexts[i - 1].requestFocus()
                    }
                    return@setOnKeyListener true
                }
                false
            }
        }

        binding.buttonVerificationValidate.setOnClickListener {
            val enteredCode = editTexts.joinToString("") { it.text.toString() }
            if (enteredCode.length == 6) {
                userViewModel.verifyCode(email, enteredCode)
            } else {
                Toast.makeText(this, "Veuillez entrer le code complet", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewVerificationResend.setOnClickListener {
            userViewModel.resendVerificationCode(email)
        }

        // Observer l'événement de vérification
        userViewModel.codeVerificationSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                val intent = Intent(this, DefineProfileActivity::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Le code est incorrect", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
