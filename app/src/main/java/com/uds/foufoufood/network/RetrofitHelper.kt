package com.uds.foufoufood.network

import android.content.Context
import android.os.Build
import com.uds.foufoufood.activities.main.TokenManager.getToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitHelper {
    companion object {
        private fun getBaseUrl(): String {
            return "http://10.0.2.2:3000"
        }

        private fun isEmulator(): Boolean {
            return (Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                    || Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.BOARD == "QC_Reference_Phone"
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.HOST.startsWith("Build") || Build.BRAND.startsWith("generic")
                    || Build.DEVICE.startsWith("generic")
                    || "google_sdk" == Build.PRODUCT)
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
                        "Authorization",
                        "Bearer $token"
                    )  // Ajoute le token JWT dans l'en-tête
                }

                chain.proceed(requestBuilder.build())
            }

            // Ajoute l'intercepteur au client OkHttp
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            // Retourne une instance Retrofit avec OkHttp et l'intercepteur
            return Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
