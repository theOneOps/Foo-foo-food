package com.uds.foufoufood.activities.main

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    // Sauvegarder le token JWT
    fun saveToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    // Récupérer le token JWT
    fun getToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }
}
