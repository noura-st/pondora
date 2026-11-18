package com.example.pondora.data.remote

import com.example.pondora.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

object GeminiClient {

    val model by lazy {
        GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }
}