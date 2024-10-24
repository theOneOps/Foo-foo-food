package com.uds.foufoufood.data_class.model

enum class OrderStatus(val displayName: String) {
    WAITING("En attente d'un livreur"),
    PREPARING("En cours de preparation"),
    PREPARED("En attente de livraison"),
    DELIVERING("En cours de livraison"),
    DELIVERED("Livrée")
}
