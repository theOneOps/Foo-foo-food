package com.uds.foufoufood.data_class.response

import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.User

data class AssignOrderResponse(
    val message: String,
    val order: Order?,
    val deliveryPerson: User?
)
