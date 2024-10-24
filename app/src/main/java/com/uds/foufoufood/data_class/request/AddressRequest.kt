package com.uds.foufoufood.data_class.request

data class AddressRequest(
    val number : Number,
    val street : String,
    val city : String,
    val country : String,
    val state : String,
    val zipCode : String
)