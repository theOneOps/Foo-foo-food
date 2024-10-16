package com.uds.foufoufood.model

data class Restaurant(
    val id: Int,
    val name: String,
    val category: String, // The category to which the restaurant belongs (e.g., "Pizza")
    val imageResId: Int   // Resource ID for the restaurant image (e.g., R.drawable.restaurant_image)
)
