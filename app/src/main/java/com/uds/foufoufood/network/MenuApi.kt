package com.uds.foufoufood.network

import com.uds.foufoufood.data_class.request.MenuRequest
import com.uds.foufoufood.data_class.response.MenuResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MenuApi{
    @GET("api/menus/menusById/{restaurantId}")
    suspend fun getAllMenusByRestaurant(
        @Header("Authorization") token:String,
        @Path("restaurantId") restaurantId:String
    ):Response<MenuResponse>

    @GET("api/menus/getAll")
    suspend fun getAllMenus():Response<MenuResponse>

    @POST("api/menus/")
    suspend fun createMenu(
        @Header("Authorization") token:String,
        @Body request:MenuRequest
    ):Response<MenuResponse>

    @PUT("api/menus/{menuId}")
    suspend fun updateMenu(
        @Header("Authorization") token:String,
        @Path("menuId") menuId:String,
        @Body request:MenuRequest
    ):Response<MenuResponse>

    @DELETE("api/menus/{menuId}")
    suspend fun deleteMenu(
        @Header("Authorization") token:String,
        @Path("menuId") menuId:String
    ):Response<MenuResponse>
}