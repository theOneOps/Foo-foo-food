package com.uds.foufoufood.activities.main

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    // TOKEN
    // Save the JWT token
    fun saveToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    // Retrieve the JWT token
    fun getToken(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }

    // Delete the JWT token
    fun deleteToken(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.apply()
    }

    fun updateToken(context: Context, token: String) {
        deleteToken(context)
        saveToken(context, token)
    }

    // ID
    // Save the user ID
    fun saveUserId(context: Context, userId: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userid", userId)
        editor.apply()
    }

    // Request the user ID
    fun getUserId(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userid", null)
    }
}
