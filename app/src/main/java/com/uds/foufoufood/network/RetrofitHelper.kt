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
            return Constants.BASE_IP_ADDRESS
        }

        fun getRetrofitInstance(context: Context): Retrofit {

            // Create an interceptor to log the requests/responses
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level =
                    // Display the body of the request/response
                    HttpLoggingInterceptor.Level.BODY
            }

            // Interceptor to add the JWT token to the request
            val authInterceptor = okhttp3.Interceptor { chain ->
                val token = getToken(context)
                val requestBuilder = chain.request().newBuilder()

                if (token != null) {
                    // Add the token to the request header
                    requestBuilder.addHeader(
                        "Authorization", "Bearer $token"
                    )
                }
                chain.proceed(requestBuilder.build())
            }

            // Add the interceptors to the OkHttp client
            val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor).build()

            // Return the Retrofit instance
            return Retrofit.Builder().baseUrl(getBaseUrl()).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}
