package com.uds.foufoufood.data_class.model

import com.google.gson.annotations.SerializedName


data class Order(
    @SerializedName("deliveryManEmail") val deliveryManEmail: String?,
    @SerializedName("_id") val orderId: String, // Correction : utilisez SerializedName pour mapper "_id"
    @SerializedName("dishes") val items: List<OrderItem>, // Correction : "dishes" au lieu de "items"
    val clientEmail: String,
    val clientName: String,
    val restaurantName: String,
    @SerializedName("restaurantAddress") val restaurantAddress: Address,
    @SerializedName("deliveryAddress") val deliveryAddress: Address,
    var status: OrderStatus // Enum optionnelle si nécessaire
)