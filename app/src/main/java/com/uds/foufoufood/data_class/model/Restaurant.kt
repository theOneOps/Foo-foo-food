package com.uds.foufoufood.data_class.model

data class Restaurant(
    val name:String,
    val address: Address,
    val speciality:String,
    val phone:String,
    val openingHours:String,
    val items:List<Menu>,
    val rating: Double,
    val reviews:List<String>,
    val imageUrl: String,
    val userId:String,
    val _id:String
)