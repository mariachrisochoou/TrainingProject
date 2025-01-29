package com.magazinestore.app.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private const val BASE_URL = "ADD_API_LINK"

    private fun getClient(token: String?): OkHttpClient {
        return OkHttpClient.Builder().apply {

            if (!token.isNullOrEmpty()) {
                addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(newRequest)
                }
            }
        }.build()
    }

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getClient(null))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}
