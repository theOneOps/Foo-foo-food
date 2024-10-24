package com.uds.foufoufood.data_class.request

data class PasswordRequest(
    val previousPassword: String,
    val newPassword: String
)