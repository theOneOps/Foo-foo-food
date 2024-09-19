package com.uds.foufoufood.models

data class User(
    var name: String,
    var email: String,
    var password: String,
    var phone: String? = null,
    var address: String? = null,
    var isAdmin: Boolean,
    var isAdminPlatform: Boolean = false, // Optionnel
    var restaurantId: String? = null
) {
    init {
        // Validation pour 'name'
        require(name.isNotBlank()) { "Name is required." }
        name = name.lowercase()

        // Validation pour 'email'
        require(email.isNotBlank() && isValidEmail(email)) { "Invalid email." }
        email = email.lowercase()

        // Validation pour 'password'
        require(password.isNotBlank()) { "Password is required." }

        // Validation pour 'phone'
        if (!phone.isNullOrBlank() && !isValidPhone(phone!!)) {
            throw IllegalArgumentException("$phone is not a valid phone number!")
        }

        // Validation conditionnelle pour 'restaurantId'
        if (isAdmin && restaurantId.isNullOrBlank()) {
            throw IllegalArgumentException("Restaurant ID is required for admins.")
        }
    }

    companion object {
        // Simulateur de fonction pour vérifier si l'email est valide
        fun isValidEmail(email: String): Boolean {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
            return email.matches(emailRegex)
        }

        // Simulateur de fonction pour vérifier si le numéro de téléphone est valide
        fun isValidPhone(phone: String): Boolean {
            val phoneRegex = "^\\+[1-9][0-9]{10,14}$".toRegex()
            return phone.matches(phoneRegex)
        }
    }
}