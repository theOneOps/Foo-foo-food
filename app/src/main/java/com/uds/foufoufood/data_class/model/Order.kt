package com.uds.foufoufood.data_class.model

data class Order(
    val orderId: String,
    val items: List<Pair<Menu, Int>>,
    val clientEmail: String,
    val clientName: String,
    val restaurantName: String,
    val restaurantAddress: Address,
    val deliveryAddress: Address,
    var status: OrderStatus = OrderStatus.WAITING
)