package com.uds.foufoufood.activities.auth

import UserRepository
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uds.foufoufood.R
import com.uds.foufoufood.databinding.LayoutDefineProfileBinding
import com.uds.foufoufood.factory.UserViewModelFactory
import com.uds.foufoufood.models.User
import com.uds.foufoufood.network.RetrofitHelper
import com.uds.foufoufood.network.UserApi
import com.uds.foufoufood.network.UserServiceImpl
import com.uds.foufoufood.viewmodel.UserViewModel

class DefineProfileActivity : AppCompatActivity() {

    private lateinit var binding: LayoutDefineProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = LayoutDefineProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_bg)

        // Récupération de l'email
        email = intent.getStringExtra("email") ?: throw IllegalArgumentException("Email is missing")

        // Initialisation du ViewModel
        val retrofit = RetrofitHelper.getRetrofitInstance()
        val userApi = retrofit.create(UserApi::class.java)
        val userService = UserServiceImpl(userApi)
        val userRepository = UserRepository(userService)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java)

        binding.radioGroupTypeProfile.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonCustomer -> binding.textViewResult.text = getString(R.string.customer_choice)
                R.id.radioButtonOwner -> binding.textViewResult.text = getString(R.string.owner_choice)
                R.id.radioButtonDeliveryMan -> binding.textViewResult.text = getString(R.string.delivery_choice)
            }
        }

        binding.buttonValidateChoiceProfile.setOnClickListener {
            val selectedProfile = when (binding.radioGroupTypeProfile.checkedRadioButtonId) {
                R.id.radioButtonCustomer -> "customer"
                R.id.radioButtonOwner -> "owner"
                R.id.radioButtonDeliveryMan -> "delivery"
                else -> {
                    Toast.makeText(this, "Veuillez choisir un type de profil", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            userViewModel.completeRegistration(email, selectedProfile)
        }

        // Observer l'événement de succès de l'enregistrement final
        userViewModel.registrationCompleteSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Échec de l'inscription", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

