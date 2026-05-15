package com.hastakala.shop.network

import org.json.JSONArray
import org.json.JSONObject

data class ClaudeMessage(
    val role: String,
    val content: String
)

data class ClaudeRequest(
    val model: String = "claude-sonnet-4-20250514",
    val maxTokens: Int = 512,
    val messages: List<ClaudeMessage>
) {
    fun toJson(): String {
        val json = JSONObject()
        json.put("model", model)
        json.put("max_tokens", maxTokens)

        val messagesArray = JSONArray()
        for (msg in messages) {
            val msgJson = JSONObject()
            msgJson.put("role", msg.role)
            msgJson.put("content", msg.content)
            messagesArray.put(msgJson)
        }
        json.put("messages", messagesArray)
        return json.toString()
    }
}

data class ClaudeResponse(
    val text: String,
    val isError: Boolean = false
) {
    companion object {
        fun fromJson(jsonString: String): ClaudeResponse {
            return try {
                val json = JSONObject(jsonString)

                // Check for error
                if (json.has("error")) {
                    val errorMsg = json.getJSONObject("error").optString("message", "Unknown API error")
                    return ClaudeResponse(text = errorMsg, isError = true)
                }

                val contentArray = json.getJSONArray("content")
                val textBuilder = StringBuilder()
                for (i in 0 until contentArray.length()) {
                    val block = contentArray.getJSONObject(i)
                    if (block.getString("type") == "text") {
                        textBuilder.append(block.getString("text"))
                    }
                }
                ClaudeResponse(text = textBuilder.toString())
            } catch (e: Exception) {
                ClaudeResponse(text = "Failed to parse API response", isError = true)
            }
        }

        fun error(message: String): ClaudeResponse {
            return ClaudeResponse(text = message, isError = true)
        }
    }
}
