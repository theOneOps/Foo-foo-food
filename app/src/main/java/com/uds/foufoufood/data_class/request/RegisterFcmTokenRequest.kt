package com.uds.foufoufood.data_class.request

data class RegisterFcmTokenRequest(
    val email: String,
    val fcmToken: String
)
