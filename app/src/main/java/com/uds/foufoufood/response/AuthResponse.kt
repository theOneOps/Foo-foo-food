package com.uds.foufoufood.response

import com.uds.foufoufood.models.User

data class AuthResponse(
    val user: User,
    val token: String
)