package com.example.autochindy.data.remote.openai

import com.google.gson.annotations.SerializedName

data class ChatCompletionRequest(
    @SerializedName("model") val model: String = "gpt-4o",
    @SerializedName("temperature") val temperature: Double = 0.0,
    @SerializedName("response_format") val responseFormat: ResponseFormat? = null,
    @SerializedName("messages") val messages: List<Message>
)

data class ResponseFormat(
    @SerializedName("type") val type: String
)

data class Message(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

data class ChatCompletionResponse(
    @SerializedName("choices") val choices: List<Choice>
)

data class Choice(
    @SerializedName("message") val message: Message
)

data class JsonBulletinResponse(
    val title: String,
    val subtitle: String,
    val content: String,
    val confidence: String
)
