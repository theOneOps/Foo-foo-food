package com.uds.foufoufood.data_class.model

data class Address(
    var number: Number? = null,
    var street: String? = null,
    var city: String? = null,
    var zipCode: String? = null,
    var state: String? = null,
    var country: String? = null
) {
    override fun toString(): String {
        return buildString {
            if (number != null) {
                append(number).append(", ")
            }
            if (!street.isNullOrEmpty()) {
                append(street).append("\n")
            }
            if (!city.isNullOrEmpty()) {
                append(city).append(", ")
            }
            if (!state.isNullOrEmpty()) {
                append(state).append(" ")
            }
            if (!zipCode.isNullOrEmpty()) {
                append(zipCode).append(", ")
            }
            if (!country.isNullOrEmpty()) {
                append(country)
            }
        }.trimEnd(',', ' ') // Supprime la virgule et l'espace final
    }
}