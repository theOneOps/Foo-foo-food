package com.uds.foufoufood.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
        private const val BASE_URL: String = "http://10.0.2.2:3000"  // Adresse du serveur local

        fun getRetrofitInstance(): Retrofit {
            // Crée un intercepteur de logging
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY  // Affiche les détails complets des requêtes/réponses
            }

            // Ajoute l'intercepteur au client OkHttp
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            // Retourne une instance Retrofit avec OkHttp et l'intercepteur
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
