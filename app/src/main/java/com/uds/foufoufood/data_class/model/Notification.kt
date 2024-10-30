package com.uds.foufoufood.data_class.model

data class Notification(
    val id: String, val message: String, val timestamp: Long, val isRead: Boolean = false
)
