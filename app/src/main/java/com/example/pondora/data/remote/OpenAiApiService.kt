package com.example.pondora.data.remote

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApiService {

    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun createResponse(
        @Body request: OpenAiRequest
    ): OpenAiResponse
}

data class OpenAiRequest(
    val model: String = "google/gemma-4-31b-it:free",
    val messages: List<OpenAiMessage>
)

data class OpenAiMessage(
    val role: String = "user",
    val content: String
)

data class OpenAiResponse(
    val choices: List<OpenAiChoice>?
)

data class OpenAiChoice(
    val message: OpenAiMessage?
)