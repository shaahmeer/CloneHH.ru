package com.example.jobtest.core.network

import com.example.jobtest.core.datasource.FileDownloadService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "https://drive.usercontent.google.com/u/0/"

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Set write timeout
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Attach OkHttpClient with timeout configurations
            .addConverterFactory(GsonConverterFactory.create()) // Gson for JSON parsing
            .build()
    }

    val fileDownloadService: FileDownloadService by lazy {
        retrofit.create(FileDownloadService::class.java)
    }
}
