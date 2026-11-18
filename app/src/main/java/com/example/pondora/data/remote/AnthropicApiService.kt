package com.example.pondora.data.remote

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AnthropicApiService {

    @Headers(
        "anthropic-version: 2023-06-01",
        "content-type: application/json"
    )
    @POST("v1/messages")
    suspend fun sendMessage(
        @Body request: AnthropicRequest
    ): AnthropicResponse
}

data class AnthropicRequest(
    val model: String = "claude-sonnet-4-20250514",
    val max_tokens: Int = 1024,
    val messages: List<Message>
)

data class Message(
    val role: String = "user",
    val content: String
)

data class AnthropicResponse(
    val content: List<ContentBlock>
)

data class ContentBlock(
    val type: String,
    val text: String
)