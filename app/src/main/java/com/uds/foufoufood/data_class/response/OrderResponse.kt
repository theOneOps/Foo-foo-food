package com.uds.foufoufood.data_class.response

import com.google.gson.annotations.SerializedName
import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.OrderItem
import com.uds.foufoufood.data_class.model.OrderStatus

data class OrderResponse(
    @SerializedName("_id") val orderId: String,
    @SerializedName("dishes") val items: List<OrderItem>,
    val clientEmail: String,
    val clientName: String,
    val restaurantName: String,
    val restaurantAddress: Address,
    val deliveryAddress: Address,
    val status: OrderStatus
)

