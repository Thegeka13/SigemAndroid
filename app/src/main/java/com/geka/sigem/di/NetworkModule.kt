package com.geka.sigem.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.geka.sigem.data.remote.EventoApi
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

    private const val BASE_URL = "https://sigembackend-production.up.railway.app/"

    // ----------------------------
    // OKHTTP CLIENT + CHUCKER
    // ----------------------------
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val chucker = ChuckerInterceptor.Builder(context)
            .maxContentLength(250_000L)
            .alwaysReadResponseBody(true)
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(chucker)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // ----------------------------
    // RETROFIT
    // ----------------------------
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // ----------------------------
    // APIs — Marketplace + Evento
    // ----------------------------
    @Provides
    @Singleton
    fun provideMarketplaceApi(retrofit: Retrofit): MarketplaceApi =
        retrofit.create(MarketplaceApi::class.java)

    @Provides
    @Singleton
    fun provideEventoApi(retrofit: Retrofit): EventoApi =
        retrofit.create(EventoApi::class.java)
}
