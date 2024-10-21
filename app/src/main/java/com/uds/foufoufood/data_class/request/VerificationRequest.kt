package com.uds.foufoufood.data_class.request

data class VerificationRequest(
    val email: String,
    val code: String
)