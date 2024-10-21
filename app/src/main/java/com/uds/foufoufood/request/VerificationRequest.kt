package com.uds.foufoufood.request

data class VerificationRequest(
    val email: String,
    val code: String
)