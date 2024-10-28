package com.uds.foufoufood.data_class.response

import com.uds.foufoufood.data_class.model.Order
import com.uds.foufoufood.data_class.model.User

data class AssignOrderResponse(
    val message: String,
    val order: Order?,           // Order est un modèle représentant une commande
    val deliveryPerson: User?     // User est un modèle représentant un utilisateur livreur
)
