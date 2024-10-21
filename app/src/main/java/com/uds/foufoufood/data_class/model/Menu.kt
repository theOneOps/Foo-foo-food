package com.uds.foufoufood.data_class.model

data class Menu(
    val name:String,
    val descriptor: String,
    val price: Double,
    val category: String,
    val image:String,
    val restaurantId:String
)
