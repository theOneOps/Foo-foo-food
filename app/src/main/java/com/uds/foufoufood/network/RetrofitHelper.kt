package com.uds.foufoufood.network

import android.content.Context
import com.uds.foufoufood.activities.main.TokenManager.getToken
import com.uds.foufoufood.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitHelper {
    companion object {
        private fun getBaseUrl(): String {
            return "http://10.0.2.2:3000" // Pour l'émulateur
            //return Constants.BASE_IP_ADDRESS // Pour son propre téléphone /!\ MODIFIER SELON SON ADRESSE IP
            //return if (isEmulator()) {
            //    "http://10.0.2.2:3000"
            //return "http://192.168.6.215"
        }

        fun getRetrofitInstance(context: Context): Retrofit {

            // Crée un intercepteur de logging
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BODY  // Affiche les détails complets des requêtes/réponses
            }

            // Intercepteur pour injecter le token JWT dans l'en-tête Authorization
            val authInterceptor = okhttp3.Interceptor { chain ->
                val token = getToken(context)  // Récupère dynamiquement le token JWT
                val requestBuilder = chain.request().newBuilder()

                if (token != null) {
                    requestBuilder.addHeader(
                        "Authorization", "Bearer $token"
                    )  // Ajoute le token JWT dans l'en-tête
                }

                chain.proceed(requestBuilder.build())
            }

            // Ajoute l'intercepteur au client OkHttp
            val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor).build()

            // Retourne une instance Retrofit avec OkHttp et l'intercepteur
            return Retrofit.Builder().baseUrl(getBaseUrl()).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}
