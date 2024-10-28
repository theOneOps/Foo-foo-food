package com.uds.foufoufood.data_class.model

import com.google.gson.annotations.SerializedName

enum class OrderStatus(val displayName: String) {
    @SerializedName("en attente d'un livreur")
    WAITING("en attente d'un livreur"),

    @SerializedName("en cours de préparation")
    PREPARING("en cours de préparation"),

    @SerializedName("en cours de livraison")
    DELIVERING("en cours de livraison"),

    @SerializedName("livrée")
    DELIVERED("livrée");

    companion object {
        fun fromDisplayName(displayName: String): OrderStatus? {
            return values().firstOrNull { it.displayName == displayName }
        }
    }
}