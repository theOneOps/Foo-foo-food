package com.uds.foufoufood.data_class.request

data class OrderUpdateRequest(
    val orderId: String, val status: String, val deliveryManEmail: String? = null
)
