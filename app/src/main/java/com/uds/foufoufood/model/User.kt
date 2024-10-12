package com.uds.foufoufood.model


data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String,
    var role: String
)
