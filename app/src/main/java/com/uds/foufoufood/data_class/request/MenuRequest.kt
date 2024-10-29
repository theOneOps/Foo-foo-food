package com.uds.foufoufood.data_class.request

data class MenuRequest(
   val name:String,
   val price:Double,
   val description:String,
   val restaurantId:String,
   val category: String,
   val image: String,
   val ingredients: List<String>
)
