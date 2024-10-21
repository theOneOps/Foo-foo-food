package com.uds.foufoufood.data_class.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val name: String,
    val email: String,
    val password: String,
    var code: String,
    val avatarUrl: String,
    var role: String? = null,
    var address: Address? = null,
    var emailValidated:Boolean?= false
)