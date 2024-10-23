package com.uds.foufoufood.data_class.model

data class Menu(
    val _id:String,
    val name:String,
    val description: String,
    val price: Double,
    val category: String,
    val restaurantId:String,
    val image:String?="",
)
