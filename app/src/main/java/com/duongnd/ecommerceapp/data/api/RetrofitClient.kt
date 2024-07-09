package com.duongnd.ecommerceapp.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL1 = "http://192.168.99.52:8080/"
    private const val BASE_URL2 = "https://e-comerce-backend-fy5g.onrender.com/"

    private val retrofitClient1: Retrofit.Builder by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()
        val gson = GsonBuilder()
            .setDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS'Z'")
            .setLenient()
            .create()
        Retrofit.Builder()
            .baseUrl(BASE_URL1)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(GsonConverterFactory.create())
    }
    val apiService: ApiService by lazy {
        retrofitClient1
            .build()
            .create(ApiService::class.java)
    }
}