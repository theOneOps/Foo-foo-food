package com.uds.foufoufood.models

data class AuthResponse(
    val user: User,
    val token: String
)