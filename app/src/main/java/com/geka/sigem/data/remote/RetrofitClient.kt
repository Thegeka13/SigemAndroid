package com.geka.sigem.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://sigembackend-production.up.railway.app/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Servicio de Cursos
    val api: CursoApiService by lazy {
        retrofit.create(CursoApiService::class.java)
    }

    // Servicio de Login (Nuevo)
    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    // Servicio de Solicitudes
    val solicitudApi: SolicitudApiService by lazy {
        retrofit.create(SolicitudApiService::class.java)
    }

}
