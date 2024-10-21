package com.uds.foufoufood.request

data class RegistrationRequest(
    val name: String,
    val email: String,
    val password: String
)