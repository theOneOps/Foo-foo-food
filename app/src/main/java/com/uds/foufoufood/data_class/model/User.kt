package com.uds.foufoufood.data_class.model

data class User(
    val name: String,
    val email: String,
    val password: String,
    var code: String,
    val avatarUrl: String?,
    var role: String? = null,
    var address: Address? = null,
    var emailValidated:Boolean?= false,
    val deliveryAvailability: Boolean? = null // Nullable pour les non-livreurs
)