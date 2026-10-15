package com.zenith26.fest.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton client managing Retrofit instance and allowing dynamic configuration
 * of the base URL for emulator (10.0.2.2:8001) or physical device IP.
 */
object ApiClient {

    const val DEFAULT_TUNNEL_BASE_URL = "https://wife-degrees-plaza-signal.trycloudflare.com/"
    const val DEFAULT_WIFI_BASE_URL = "http://172.16.54.116:8001/"
    const val DEFAULT_EMULATOR_BASE_URL = "http://10.0.2.2:8001/"
    const val DEFAULT_LOCALHOST_BASE_URL = "http://127.0.0.1:8001/"

    private var currentBaseUrl: String = DEFAULT_TUNNEL_BASE_URL

    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private var retrofit: Retrofit = buildRetrofit(currentBaseUrl)
    private var apiService: ZenithApiService = retrofit.create(ZenithApiService::class.java)

    private fun buildRetrofit(url: String): Retrofit {
        val sanitized = if (url.endsWith("/")) url else "$url/"
        return Retrofit.Builder()
            .baseUrl(sanitized)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getService(): ZenithApiService = apiService

    fun getBaseUrl(): String = currentBaseUrl

    @Synchronized
    fun setBaseUrl(newUrl: String) {
        val sanitized = if (newUrl.endsWith("/")) newUrl else "$newUrl/"
        currentBaseUrl = sanitized
        retrofit = buildRetrofit(sanitized)
        apiService = retrofit.create(ZenithApiService::class.java)
    }
}
