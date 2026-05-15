package com.hastakala.shop.network

import com.hastakala.shop.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class ClaudeApiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val apiKey: String = BuildConfig.CLAUDE_API_KEY

    val isConfigured: Boolean get() = apiKey.isNotBlank()

    suspend fun getInsight(prompt: String): ClaudeResponse = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext ClaudeResponse.error("API key not configured")
        }

        try {
            val claudeRequest = ClaudeRequest(
                messages = listOf(ClaudeMessage(role = "user", content = prompt))
            )

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = claudeRequest.toJson().toRequestBody(mediaType)

            val request = Request.Builder()
                .url("https://api.anthropic.com/v1/messages")
                .addHeader("x-api-key", apiKey)
                .addHeader("anthropic-version", "2023-06-01")
                .addHeader("content-type", "application/json")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (response.isSuccessful) {
                ClaudeResponse.fromJson(responseBody)
            } else {
                ClaudeResponse.fromJson(responseBody)
            }
        } catch (e: Exception) {
            ClaudeResponse.error("Network error: ${e.localizedMessage ?: "Connection failed"}")
        }
    }
}
