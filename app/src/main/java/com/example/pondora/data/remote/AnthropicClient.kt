package com.example.pondora.data.remote

import com.example.pondora.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AnthropicClient {

    // BODY logging must stay off in release builds: it would print the
    // x-api-key header and the full request/response to logcat.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-api-key", BuildConfig.ANTHROPIC_API_KEY)
                .build()
            chain.proceed(request)
        }
        .build()

    val service: AnthropicApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.anthropic.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnthropicApiService::class.java)
    }
}