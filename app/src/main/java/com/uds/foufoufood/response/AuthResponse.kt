package com.uds.foufoufood.response

import com.uds.foufoufood.model.User

data class AuthResponse(
    val user: User,
    val token: String
)