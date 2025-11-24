package com.geka.sigem.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor // Importar Chucker
import com.geka.sigem.data.remote.MarketplaceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context // 1. Inyectamos el contexto aquí
    ): OkHttpClient {

        // Logcat normal
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 2. Creamos el interceptor de Chucker
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .maxContentLength(250000L) // Para que lea JSONs largos
            .alwaysReadResponseBody(true)
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(logging) // Logs en consola
            .addInterceptor(chuckerInterceptor) // <--- ¡LA MAGIA AQUÍ! Logs en Notificación
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://sigembackend-production.up.railway.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMarketplaceApi(retrofit: Retrofit): MarketplaceApi {
        return retrofit.create(MarketplaceApi::class.java)
    }
}