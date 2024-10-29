package com.uds.foufoufood.data_class.response

import com.uds.foufoufood.data_class.model.User

data class LoginGoogleResponse(
    val user: User,
    val token: String
)