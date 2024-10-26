package com.uds.foufoufood.data_class.request

import com.uds.foufoufood.data_class.model.Address
import com.uds.foufoufood.data_class.model.OrderItem

data class OrderRequest(
    val clientEmail: String,
    val clientName: String,
    val restaurantId: String,
    val deliveryAddress: Address,
    val dishes: List<OrderItem>,
    val status: String = "en attente d'un livreur"
)
