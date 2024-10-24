package com.uds.foufoufood.data_class.response

import com.uds.foufoufood.data_class.model.Menu



data class MenuResponse(
    val data: List<Menu>?,
    val message: String = "",
    val success: Boolean = false
)